package rockTheJVM.handyTypeclasses

import cats.{Foldable, Traverse}
import topics.functors.Functor

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Traversing {

  // Higher level approach to iteration - but dont think about iteration

  // So when designing abstract functions you should design them to work with the weakest typeclass in mind
  // not the most restrictive obviously if it makes sense to do so - rule of thumb

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  val servers = List("server-ci-1", "server-staging-2", "server-prod-3")

  def getBandwidth(hostname: String): Future[Int] = Future(hostname.length * 80)

  val allBandwidths: Future[List[Int]] = servers.foldLeft(Future(List.empty[Int])) {
    // Janky since this trasforms each element to a Future[Int] then combining all these F[Ints] into a list through appending and creating a ton of futures
    // Manual solution
    /*
    - List[String]
    - a f = String => Future[Int]
    we want a Future[List[Int]]
     */

    (acc, hostname) =>
      val bandFuture: Future[Int] = getBandwidth(hostname)
      val desired: Future[List[Int]] =
        for {
          accBandwidths: List[Int] <- acc
          band: Int <- bandFuture
        } yield accBandwidths :+ band
      desired
  }

  val allBandwidthTraverse: Future[List[Int]] = Future.traverse(servers)(getBandwidth) // flips the types on the List Future
  val allBandwidthSeq: Future[List[Int]] = Future.sequence(servers.map(getBandwidth)) // we have a List[Future[Int]] which flips to Future[List[Int]]

  // we will now look at a more higher level, abstract version

  import cats.syntax.applicative._ // pure


  //I have 2 ingredients a "List(1,2,3)" and "function: Int => Option[Boolean]" return and give me a Option[List[Boolean]] so do a switcheroo nesting on the outer types BUT
  // I want this to work on any outer types List, Future, Option, Vector etc. so long as they are Monads or monadic in nature i.e they have the flatmap method which
  // allows the use of a "for comprehension"

  /*  Monad version but...

    import cats.syntax.flatMap._
    import cats.syntax.functor._
    import cats.Monad

     def listTraverse[F[_] : Monad, A, B](list: List[A])(func: A => F[B]): F[List[B]] = {

      // honestly manually adding  types is hard so let the compiler do the work and try to think abstractly then add them in after, follow the types CAREFULLY!!!

      list.foldLeft(List.empty[B].pure[F]) { (wrappedAcc: F[List[B]], element: A) =>

        val fb: F[B] = func(element)

        for {
          acc <- wrappedAcc
          elem <- fa
        } yield acc :+ elem
      }
    }

    */

  // traverse is actually a lower minimum bound not Monad it is actually Applicative, we can use something else for combination which is .mapN()

  import cats.Applicative
  import cats.syntax.apply._ //mapN

  // because we have a weaker category typeclass conext bound we which is also available on List, Vector, Future, Option etc. this opens it up to be used on Validated
  // Validated is not a Monad so the one above implementation would not be able to traverse Validated[E, A] structures but this one can since Validated are Applicative Functors

  def listTraverse[F[_] : Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] = {

    // no monad context bound so for comprehension breaks so we use .mapN()
    list.foldLeft(List.empty[B].pure[F]) { (wrappedAcc: F[List[B]], element: A) =>

      val wElement: F[B] = func(element)
      (wrappedAcc, wElement).mapN(_ :+ _)
    }
  }

  //easier version of traverse
  def listSequenceMikey[F[_] : Applicative, A](list: List[A]): F[List[A]] = {

    list.foldLeft(List.empty[A].pure[F]) { (wrappedAcc: F[List[A]], element: A) =>
      val wElement = element.pure[F]
      (wrappedAcc, wElement).mapN(_ :+ _)
    }
  }

  def listSequence[F[_] : Applicative, A](list: List[F[A]]): F[List[A]] = {
    listTraverse(list)(identity)
  }

  val allPairs = listSequence(List(Vector(1, 2), Vector(3, 4))) // Vector(List( (1,3), (1,4), (2,3), (2,4) )
  val threeTuples = listSequence(List(Vector(1, 2), Vector(3, 4), Vector(5, 6))) // all the possible truples

  def filterAsOption(list: List[Int])(predicate: Int => Boolean): Option[List[Int]] = {
    // this will check all meet the predicate t return all elements as a List otherwise return None if not all satisfy
    listTraverse[Option, Int, Int](list)(n => Some(n).filter(predicate))
  }

  val isEven: Int => Boolean = _ % 2 == 0
  val allTrue = filterAsOption(List(2, 4, 6))(isEven) // Some(List(2,4,6))
  val someFalse = filterAsOption(List(1, 2, 3))(isEven) // None

  import cats.data.Validated
  import cats.instances.list._ // Semigroup[List] => Applicative[ErrorsOr]

  type ErrorsOr[T] = Validated[List[String], T]

  def filterAsValidated(list: List[Int])(predicate: Int => Boolean): ErrorsOr[List[Int]] = {
    listTraverse[ErrorsOr, Int, Int](list) { n =>
      if (predicate(n)) Validated.valid(n)
      else Validated.invalid(List(s"predicate for $n failed"))
    }
  }

  val allTrueValidated = filterAsValidated(List(2, 4, 6))(isEven) // Valid(List(2,4,6))
  val someFalseValidated = filterAsValidated(List(1, 2, 3))(isEven) // Invalid(List("predicate for 1 failed", "predicate for 3 failed"))

  trait MyTraverse[L[_]] extends Foldable[L] with Functor[L] {

    def traverse[F[_] : Applicative, A, B](container: L[A])(func: A => F[B]): F[L[B]]

    def sequence[F[_] : Applicative, A, B](container: L[F[A]]): F[L[A]] =
      traverse(container)(identity)

    type Identity[T] = T

    def mapExercise[A, B](wa: L[A])(f: A => B): L[B] = {
      traverse[Identity, A, B](wa)(f)
    }

    import cats.Id // same as Identity[T]

    def mapCats[A, B](wa: L[A])(f: A => B): L[B] = {
      traverse[Id, A, B](wa)(f)
    }

    // because we can implement Map from traverse, the typeclass Traverse naturally is a Functor

  }

  import cats.instances.future._

  val allBandwidthsCats: Future[List[Int]] = Traverse[List].traverse(servers)(getBandwidth)

  import cats.syntax.traverse._ //sequence + traverse

  val allBandwidthsCats2: Future[List[Int]] = servers.traverse(getBandwidth)

  def main(args: Array[String]): Unit = {

    println(allPairs)
    println(threeTuples) // the behaviors are wackier depending on the elements

    println(allTrue)
    println(someFalse)

    println(allTrueValidated)
    println(someFalseValidated)
  }
}
