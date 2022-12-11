package rockTheJVM.handyTypeclasses

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Semigroupals {

  //Semigroupal is a tupling typeclass which can tuple elements within a context essentially
  //It's main function is .product whose behaviour differs depending on the context e.g. Options, Lists, Either and Validated

  // DO NOT confuse Semigroup |+| with Semigroupal F[(A, B)]

  trait MySemigroupal[F[_]] {

    def product[A, B](fa: F[A])(fb: F[B]): F[(A, B)]
  }

  import cats.Semigroupal
  import cats.instances.option._

  val optionSemigroupal = Semigroupal[Option]
  val aTupledOption = optionSemigroupal.product(Some(123), Some("a string")) // Some((123, "a string"))

  val aNoneTupled = optionSemigroupal.product(Some(123), None) // None

  import cats.instances.future._

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val aTupledFuture = Semigroupal[Future].product(Future("meaning of life"), Future(42)) // Future(("meaning of life", 42))

  import cats.instances.list._

  val aTupledList = Semigroupal[List].product(List(1, 2), List("a", "b"))
  // List((1, "a"), (1, "b"), (2, "a"), (2, "b"))

  //TODO: implement product with monads

  import cats.Monad
  import cats.syntax.flatMap._
  import cats.syntax.functor._

  def productWithMonads[F[_], A, B](fa: F[A], fb: F[B])(implicit monad: Monad[F]): F[(A, B)] = {
    // we are able to implement semigroupal in terms of monad's functions
    // hence Monad extends Semigroupal
    monad.flatMap(fa)(a => monad.map(fb)(b => (a, b)))
  }

  def productWithMonads2[F[_], A, B](fa: F[A], fb: F[B])(implicit monad: Monad[F]): F[(A, B)] = { //kinda needs the imports not entirely sure why
    for {
      a <- fa
      b <- fb
    } yield (a, b)
  }

  //Monads extends Semigroupals

  // example: Validated

  // for Validated it's semigroupal does not follow monadic laws i.e it does not follow map/flatmap
  // so it's semigroupal is able to concatenate List[Strings] which usually contains it's errors

  // It's Semigroupal is not Monadic

  import cats.data.Validated

  type ErrorsOr[T] = Validated[List[String], T]

  val validatedSemigroupal = Semigroupal[ErrorsOr] //requires the implicit Semigroup[List[_]]

  val invalidsCombination =
    validatedSemigroupal.product(
      Validated.invalid(List("Something wrong", "something else wrong")),
      Validated.invalid(List("This can't be right"))
    )

  type EitherErrorsOr[T] = Either[List[String], T]

  // However for Either it's semigroupal i.e. tupling typeclass does follow monad laws and so it fails to concatenate the second Left in teh example below
  // It's Semigroupal is a Monad

  import cats.instances.either._ //import implicit Monad[Either]

  val eitherSemigroupal = Semigroupal[EitherErrorsOr]

  val eitherCombination = eitherSemigroupal.product( // implemented in terms of map/flatmap
    Left(List("Something wrong", "something else wrong")),
    Left(List("This can't be right")) // this will short circuit and not print out the second Left
  )

  //TODO 2: define a Semigroupal[List] which does a zip

  val zipListSemigroupal: Semigroupal[List] = new Semigroupal[List] {
    override def product[A, B](fa: List[A], fb: List[B]): List[(A, B)] = fa.zip(fb)
  }

  def main(args: Array[String]): Unit = {

    println(aTupledList)
    println(productWithMonads(Option(3), Option("c")))
    println(invalidsCombination)
    println(eitherCombination)
    println(zipListSemigroupal.product(List(1, 2), List("a", "b")))

  }
}
