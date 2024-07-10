package study_time

import cats.{Applicative, Functor}
import cats.effect.IO
import cats.implicits._

import scala.util.chaining._

object Practice extends App {

  val fucntion_1: Int => Int = (i: Int) => i * 3

  val fucntion_2: Int => Int = (i: Int) => i * i

  val fucntion_3: Int => String = (i: Int) => i.toString //  Int => String

  val fucntion_4: String => Double = (s: String) => s.toDouble // String => Double

  // infix notation

  // initial input value
  //    |
  //   Int => Int    =>   Int => Int   =>      Int => Int       Int => String   String => Double
  fucntion_1 andThen fucntion_2 andThen fucntion_1 andThen fucntion_3 andThen fucntion_4 // functional composition


  // EndoFunctor / Functor preserves structure


  // Option  is a Functor
  // List  is a Functor
  // Vectors  is a Functor
  // Seq  is a Functor
  // Futures  is a Functor
  // IO  from cats effect  is a Functor

  // what is a functor - basically it's anything with  .map() method
  // what is a functor - and it's anything with  way to lift a value into the container type/context container type, in many cases it is the 'container' type's
  // .apply() method

  val Seq_Functor: Seq[Int] = Seq.apply(1)

  val applicatives = Applicative[Seq].pure(1)

//  implicit val implied: Applicative[Seq] = Applicative[Seq]

  def functors()(implicit aa: Applicative[Seq]) = Functor[Seq].pure(aa)

  val IO_Functor: IO[Int] = IO.apply(1)

  val puritan: IO[Int] = IO.pure(1)


//  Seq_Functor.tap(println)
//  applicatives.tap(println)
//  IO_Functor.tap(println)
//  puritan.tap(println)

  functors().tap(println)

//  1.tap("s").tap(println)

}


object Practice_v2 {


  def main(args: Array[String]): Unit = {


  }

}