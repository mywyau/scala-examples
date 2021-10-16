package catsStuff.applicativeExamples.apply

import cats.Applicative
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ApplicativeComposition {

  // Using some nested context values we will perform some Applicative composition

  val futureOptFive: Future[Option[Int]] = Future.successful(Some(5))
  val futureOptSeven: Future[Option[Int]] = Future.successful(Some(7))

  val composeFutureOption: Future[Option[Int]] =
    Applicative[Future].compose[Option]
      .map2(futureOptFive, futureOptSeven)(_ + _) // adds to Future(Option(12))

  // def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] = ??? takes two context values and curries a two argument function to perform the composition
  // of our nested future option.

  // pretty straightforward right?
}

object CompositionRunner extends App {

  println(ApplicativeComposition.composeFutureOption)

}
