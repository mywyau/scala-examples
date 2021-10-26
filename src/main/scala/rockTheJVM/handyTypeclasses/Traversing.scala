package rockTheJVM.handyTypeclasses

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Traversing {

  // Higher level approach to iteration - but dont think about iteration

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

  listSequence(List(Vector(1, 2), Vector(3, 4))) // Vector[List(1,2,3,4)]


  def main(args: Array[String]): Unit = {


  }
}
