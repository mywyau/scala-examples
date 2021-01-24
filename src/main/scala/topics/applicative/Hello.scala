package topics.applicative

import cats.Functor

object Playground extends ApplicativeExample with App {

  val applicative = new ApplicativeExample


  println(applicative.composedApplicativeExample) //97 + 5 composed in Option
  println(traverseOptionExample)

}

trait MyApplicative2[F[_]] extends Functor[F] {
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]

  def pure[L, A](a: A): F[A]
}

class ApplicativeExample {

  trait MyApplicative[F[_]] extends Functor[F] {

    def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]

    def pure[A](a: A): F[A] //identity

    def map[A, B](fa: F[A])(f: A => B): F[B] = ap(pure(f))(fa)

    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]

    /*pure wraps the value into the type constructor - for Option this could be Some(_), for Future Future.successful, and for List a singleton list.
    ap is a bit tricky to explain and motivate, so we’ll look at an alternative but equivalent formulation via product.
    */
  }

  // Example implementation for right-biased Either
  //  implicit def applicativeForEither[L]: MyApplicative2[Either[L, *]] = new MyApplicative2[Either[L, *]] {        // * type parameter ? not sure if outdated
  //
  //    def product[A, B](fa: Either[L, A], fb: Either[L, B]): Either[L, (A, B)] = (fa, fb) match {
  //      case (Right(a), Right(b)) => Right((a, b))
  //      case (Left(l), _) => Left(l)
  //      case (_, Left(l)) => Left(l)
  //    }
  //
  //    def pure[A](a: A): Either[L, A] = Right(a)
  //
  //  }

  trait MyApplicative2[F[_]] extends Functor[F] {

    def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]

    def pure[A](a: A): F[A] //identity

    def map[A, B](fa: F[A])(f: A => B): F[B] = ap(pure(f))(fa)
  }

  import cats.Applicative

  def product3[F[_] : Applicative, A, B, C](fa: F[A], fb: F[B], fc: F[C]): F[(A, B, C)] = {
    val F = Applicative[F]
    val fabc = F.product(F.product(fa, fb), fc)
    F.map(fabc) { case ((a, b), c) => (a, b, c) }
  }

  val f: (Int, Char) => Double = (i, c) => (i + c).toDouble

  val int: Option[Int] = Some(5)
  val char: Option[Char] = Some('a')

  val hmm: Option[Char => Double] = int.map(i => (c: Char) => f(i, c)) // what now?
  //  val hmmAP: Option[Char => Double] = int.ap(i => (c: Char) => f(i, c)) // what now?

  //We have an Option[Char => Double] and an Option[Char] to which we want to apply the function to, but map doesn’t give us enough power to do that. Hence, ap.

  import cats.implicits._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  /*  Applicatives compose
      Like Functor, Applicatives compose. If F and G have Topics.Applicative instances, then so does F[G[_]].*/

  val x: Future[Option[Int]] = Future(Some(5)) // value of 5
  val y: Future[Option[Char]] = Future(Some('a')) //ascii value of 97

  val composedApplicativeExample: Future[Option[Int]] = Applicative[Future].compose[Option].map2(x, y)(_ + _)
  // composed: scala.concurrent.Future[Option[Int]] = Future(<not completed>)


  /*  Traverse
    The straightforward way to use product and map (or just ap) is to compose n independent effects, where n is a fixed number.
    In fact there are convenience methods named apN, mapN, and tupleN (replacing N with a number 2 - 22) to make it even easier.

      Imagine we have one Option representing a username, one representing a password, and another representing a URL for logging into a database.*/

  import java.sql.Connection

  val username: Option[String] = Some("username")
  val password: Option[String] = Some("password")
  val url: Option[String] = Some("some.login.url.here")

  // Stub for demonstration purposes
  def attemptConnect(username: String, password: String, url: String): Option[Connection] = None

  // We know statically we have 3 Options, so we can use map3 specifically.

  Applicative[Option].map3(username, password, url)(attemptConnect)

  //Sometimes we don’t know how many effects will be in play - perhaps we are receiving a list from user input or getting rows from a database. This implies the need for a function:

  def sequenceOption[A](fa: List[Option[A]]): Option[List[A]] = ???

  // Alternatively..

  def traverse[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = ???

  /*  Users of the standard library Future.sequence or Future.traverse will find these names and signatures familiar.
    Let’s implement traverseOption (you can implement sequenceOption in terms of traverseOption).*/

  def traverseOption[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = {
    as.foldRight(Some(List.empty[B]): Option[List[B]]) { (a: A, acc: Option[List[B]]) =>
      val optB: Option[B] = f(a)
      // optB and acc are independent effects so we can use Topics.Applicative to compose
      Applicative[Option].map2(optB, acc)(_ :: _)
    }
  }

  val oneToFive: List[Int] = List(1, 2, 3, 4, 5)

  val traverseOptionExample: Option[List[Int]] = traverseOption(oneToFive)(i => Some(i): Option[Int]) //turns a List[Int] => Option[List[Int]]


//  def traverseEither[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] =
//    as.foldRight(Right(List.empty[B]): Either[E, List[B]]) { (a: A, acc: Either[E, List[B]]) =>
//      val eitherB: Either[E, B] = f(a)
//      Topics.Applicative[Either[E, ]].map2(eitherB, acc)(_ :: _)
//    }

}
