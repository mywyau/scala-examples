package catsnotes

import cats.data.Kleisli
import cats.implicits._

sealed trait Mikey

case object Foodles extends Mikey

case object Mokie extends Mikey

class KleisliExample {

  val function1: Int => String = (i: Int) => "hello" //arbitrary implementations
  val function2: String => Boolean = (s: String) => true
  val function3: Boolean => Foodles.type = (b: Boolean) => Foodles

  // my goal is to 'chain' we do this through function composition

  val functionComposition: Int => Foodles.type = function1 >>> function2 andThen function3 // >>> Cats Arrow for andThen

  val function1B: Int => Option[String] = (i: Int) => Option("hello") // A => F[B]
  val function2B: String => Option[Boolean] = (s: String) => Option(true)
  val function3B: Boolean => Option[Foodles.type] = (b: Boolean) => Option(Foodles)

  //val functionCompositionB: Int => Mikey = function1B andThen function2B andThen function3B    // this does not compile since the functions do not actually compose

  // but what happens when our functions have a computational context??? like A => F[B] ??
  val function1K: Kleisli[Option, Int, String] = Kleisli((_: Int) => Option("hello")) // A => F[B]   uh oh that sneaky F has foiled our plans of function composition
  val function2K: Kleisli[Option, String, Boolean] = Kleisli((_: String) => Option(true)) // we can use the Kleisli Arrow hurray
  val function3K: Kleisli[Option, Boolean, Foodles.type] = Kleisli((_: Boolean) => Option(Foodles))

  val functionCompositionK: Kleisli[Option, Int, Foodles.type] = function1K andThen function2K andThen function3K //not yet evaluated Kleisli all I have done is chain functions together
  val composeSyntax: Kleisli[Option, Int, Foodles.type] = function3K compose function2K compose function1K

  val functionCompositionArrowK: Kleisli[Option, Int, Foodles.type] = function1K >>> function2K >>> function3K // arrow syntax from cats just syntax sugar really
  val composeKArrowSyntax: Kleisli[Option, Int, Foodles.type] = function3K <<< function2K compose function1K


  val composeAllKleislis: Option[Foodles.type] = // notice how I've evaluated each Kleisli and composed them together seems a little pointless in this example
    for {
      v1: String <- function1K(1)
      v2: Boolean <- function2K(v1)
      v3: Foodles.type <- function3K(v2)
    } yield v3


  ///

  val optionAB: Option[Int] =
    for {
      a <- Option(7)
      b <- Option(10)
    } yield a + b // Some(17)

  // Kleisli is also like Option "Monadic" in nature since it lets us use this "for comprehension" structure

  val kleisliOne: Kleisli[Option, Int, String] = ??? // not caring or ignoring the implementation
  val kleisliTwo: Kleisli[Option, String, Boolean] = ???
  val kleisliThree: Kleisli[Option, Boolean, Foodles.type] = ???

  val kleisliForComp = {
    kleisliOne.flatMap(
      (x: String) => Kleisli { _: Int => Option(x) }
    )
  }

  ///

}

object KleisliRunner extends App {

  val kleisliExample = new KleisliExample

  println(kleisliExample.functionCompositionK(7).map(_ => Mokie)) // should return Some(Mokie)
  println(kleisliExample.composeSyntax(7)) // should return Some(Foodles)
  println(kleisliExample.functionComposition(7)) // No Options involved so should return Foodles
  println(kleisliExample.functionCompositionArrowK(7)) // No Options involved so should return Foodles
  println(kleisliExample.composeAllKleislis)
}

// f(x) = x + 7, g(x) = x + 10   --- f(g(x)) or f after g(x) so if x = 1 then    f(g(1)) = 18

// f(x:Int) = x:Int + 7, g(x:Int) = x.toString  ---- f(g(3)) = "10" i.e. it first does 3 + 7 = 10, then coverts the 10 integer into a string of characters
// which type wise is different to integer 10

// f(x:Int) = Option( (x:Int) + 7 ), g(x:Int) = Option(x.toString) , well we now have types of Int => Option[Int], Int => Option[String]

// we are now attempting to do the following: Int => Option[Int] andThen Int => Option[String]

// this does not follow since you cannot use an output of Option[Int] in place for an Integer input into the next calculation, so we have a wrapper called Kleisli[F[_], A, B]

// Kleisli lets this be viable: Int => Option[Int] andThen Int => Option[String] by having code and logic under the hood which has been generalised for certain data types
