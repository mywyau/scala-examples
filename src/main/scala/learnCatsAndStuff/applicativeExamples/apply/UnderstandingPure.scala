package learnCatsAndStuff.applicativeExamples.apply

import cats.Applicative
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object UnderstandingPure {

  // Applicative has a method called ".pure" this is an alias to ".bind" in Haskell.

  val makeOptionThree: Option[Int] = Applicative[Option].pure(3)
  val makeFutureSeven: Future[Int] = Applicative[Future].pure(7)
  val makeListTen: List[Int] = Applicative[List].pure(10)

  //pretty straightforward right?
  // you can use the applicative pure method to lift a value into a context nice

}

object PureRunner extends App {

  println(UnderstandingPure.makeOptionThree)
  println(UnderstandingPure.makeFutureSeven)
  println(UnderstandingPure.makeListTen)

}
