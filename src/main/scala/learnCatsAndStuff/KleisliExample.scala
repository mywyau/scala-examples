package learnCatsAndStuff

import cats.data.Kleisli
import cats.implicits._

sealed trait Mikey

case object Foodles extends Mikey

case object Mokie extends Mikey

class KleisliExample {

  val function1: Int => String = (i: Int) => "hello" //arbitrary implementations
  val function2: String => Boolean = (s: String) => true
  val function3: Boolean => Mikey = (b: Boolean) => Foodles

  // my goal is to 'chain' we do this through function composition

  val functionComposition: Int => Mikey = function1 >>> function2 andThen function3 // >>> Cats Arrow for andThen

  val function1B: Int => Option[String] = (i: Int) => Option("hello") // A => F[B]
  val function2B: String => Option[Boolean] = (s: String) => Option(true)
  val function3B: Boolean => Option[Mikey] = (b: Boolean) => Option(Foodles)

  //  val functionCompositionB: Int => Mikey = function1B andThen function2B andThen function3B    // nice we have achieved our goal of function composition

  // but what happens when our fucntions have a computational context??? like A => F[B] ??

  val function1K: Kleisli[Option, Int, String] = Kleisli((_: Int) => Option("hello")) // A => F[B]   uh oh that sneaky F has foiled our plans of function composition
  val function2K: Kleisli[Option, String, Boolean] = Kleisli((_: String) => Option(true)) // we can use the Kleisli Arrow hurray
  val function3K: Kleisli[Option, Boolean, Mikey] = Kleisli((_: Boolean) => Option(Foodles))

  val functionCompositionK: Kleisli[Option, Int, Mikey] = function1K andThen function2K andThen function3K //not yet evaluated Kleisli all I have done is chain functions together
  val composeSyntax: Kleisli[Option, Int, Mikey] = function3K compose function2K compose function1K

  val functionCompositionArrowK: Kleisli[Option, Int, Mikey] = function1K >>> function2K >>> function3K
  val composeKArrowSyntax: Kleisli[Option, Int, Mikey] = function3K <<< function2K compose function1K


  val composeAllKleislis: Option[Mikey] = // notice how I've evaluated each Kleisli and composed them together seems a little pointless in this example
    for {
      v1: String <- function1K(1)
      v2: Boolean <- function2K(v1)
      v3: Mikey <- function3K(v2)
    } yield v3

}

object KleisliRunner extends App {

  val kleisliExample = new KleisliExample

  println(kleisliExample.functionCompositionK(7).map(_ => Mokie)) // should return Some(Mokie)
  println(kleisliExample.composeSyntax(7)) // should return Some(Foodles)
  println(kleisliExample.functionComposition(7)) // No Options involved so should return Foodles
  println(kleisliExample.functionCompositionArrowK(7)) // No Options involved so should return Foodles
  println(kleisliExample.composeAllKleislis)
}
