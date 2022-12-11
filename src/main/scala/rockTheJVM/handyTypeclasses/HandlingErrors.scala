package rockTheJVM.handyTypeclasses

import cats.{Applicative, Monad}

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object HandlingErrors {

  // 3 levels of error handling maturity
  // scala dev journey level 1 try/catch blocks
  // using Try, encapsulate throwables
  // pure fp using Cats

  // E is an error type

  trait MyApplicativeError[M[_], E] extends Applicative[M] {

    def raiseError[A](e: E): M[A] // fundamental method of ApplicativeError

    def handleErrorWith[A](ma: M[A], f: E => M[A]): M[A] // fundamental method of ApplicativeError

    def handleError[A](ma: M[A], f: E => A): M[A] = handleErrorWith(ma, e => pure(f(e)))
  }

  trait MyMonadError[M[_], E] extends MyApplicativeError[M, E] with Monad[M] {

    // fundamentalError
    def ensure[A](ma: M[A])(error: E)(predicate: A => Boolean): M[A]
  }

  import cats.MonadError
  import cats.instances.either._ //implicit MonadError

  type ErrorOr[A] = Either[String, A]

  val monadErrorEither = MonadError[ErrorOr, String]
  val success = monadErrorEither.pure(32) // Either[String, Int] == Right(32)
  val failure = monadErrorEither.raiseError[Int]("something wrong") //give it the other value type just in case the monad either gets composed with something else

  // recover - similar to Futures
  val handledError: ErrorOr[Int] =
    monadErrorEither.handleError(failure) {
      case "Badness" => 44
      case _ => 89
    }

  // recoverWith - similar to Futures
  val handledError2: ErrorOr[Int] = monadErrorEither.handleErrorWith(failure) {
    case "Badness" => monadErrorEither.pure(44) //ErrorOr[Int]
    case _ => Left("Something else") // ErrorOr[Int]
  }

  // filter
  val filteredSuccess = monadErrorEither.ensure(success)("Number too small")(_ > 100)

  //Try and Future

  import cats.instances.try_._ // implicit MonadError[Try], E = Throwable

  val exeception = new RuntimeException("Really bad")

  val pureException: Try[Nothing] = MonadError[Try, Throwable].raiseError(exeception) //Try[Nothing]
  // storing it in a purely functional way rather than running it

  import cats.instances.future._

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val futureException: Future[Nothing] = MonadError[Future, Throwable].raiseError(exeception)

  // applicatives => ApplicativeError

  import cats.data.Validated

  type VErrorsOr[T] = Validated[List[String], T]

  import cats.ApplicativeError
  import cats.instances.list._ // implicit Semigroup[List] => ApplicativeError[ErrorsOr, List[String]]

  val applErrorVal = ApplicativeError[VErrorsOr, List[String]]
  // pure, raiseError, handleError, handleErrorWith

  //extension methods

  import cats.syntax.applicative._ // pure
  import cats.syntax.applicativeError._ // raiseError, handleError(With)

  val extendedSucess = 42.pure[ErrorOr]  // requires the implicit Applicative[ErrorsOr, List[String]]
  val extendedError: VErrorsOr[Int] = List("Badness").raiseError[VErrorsOr, Int]

  def main(args: Array[String]): Unit = {

  }
}
