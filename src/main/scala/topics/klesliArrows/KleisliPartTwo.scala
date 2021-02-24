package topics.klesliArrows

import cats.data.Kleisli
import cats.implicits._


/*
  Kleisli Categories extract from Category Theory by Bartosz Milewski

  You might have guessed that I haven’t invented this category on the
  spot. It’s an example of the so called Kleisli category — a category based
  on a monad. We are not ready to discuss monads yet, but I wanted to
  give you a taste of what they can do. For our limited purposes, a Kleisli
  category has, as objects, the types of the underlying programming language.
  Morphisms from type 𝐴 to type 𝐵 are functions that go from 𝐴 to
  a type derived from 𝐵 using the particular embellishment. Each Kleisli
  category defines its own way of composing such morphisms, as well as
  the identity morphisms with respect to that composition. (Later we’ll
  see that the imprecise term “embellishment” corresponds to the notion
  of an endofunctor in a category.)

  The particular monad that I used as the basis of the category in this
  post is called the writer monad and it’s used for logging or tracing the execution
  of functions. It’s also an example of a more general mechanism
  for embedding effects in pure computations. You’ve seen previously that
  we could model programming-language types and functions in the category
  of sets (disregarding bottoms, as usual). Here we have extended
  this model to a slightly different category, a category where morphisms
  are represented by embellished functions, and their composition does

*/

object KleisliPartTwo {

  //Kleisli Arrows are all about

  //Sometimes, our functions will need to return monadic values. For instance, consider the following set of functions.

  val parse: String => Option[Int] = s => if (s.matches("-?[0-9]+")) Some(s.toInt) else None //returns an Option[Int]

  val reciprocal: Int => Option[Double] = { // Needs a Int from 'val parseStringToInt' but 'val parseStringToInt' gives back Option[Int] how can we solve this?
    yourInt => if (yourInt != 0) Some(1.0 / yourInt) else None
  }

  val parseK: Kleisli[Option, String, Int] = {
    Kleisli((s: String) => if (s.matches("-?[0-9]+")) Some(s.toInt) else None) //wrap them in our Kleisli implementation
  }

  val reciprocalK: Kleisli[Option, Int, Double] = {
    Kleisli((i: Int) => if (i != 0) Some(1.0 / i) else None)
  }

  val parseAndReciprocal: Kleisli[Option, String, Double] = {
    reciprocalK.compose(parseK) //Now we can compose them, since Kleisli handles the option
  }


  //Writer embellishment

  /*
  - we will then write morphisms with type A => Writer[B]
  - and define our kleisli arrow as a method distinct from the Cats library version
  */

  // - fish operator aka Kleisli, A = Object/Type A, B = Object/Type B , C = Object/Type C, m1 = morphism 1, m2 = morphism 2

  type Writer[A] = (A, String)

  object kleisli {

    implicit class KleisliOps[A, B](morphism1: A => Writer[B]) {

      def >=>[C](morphism2: B => Writer[C]): A => Writer[C] = {
        x: A => {
          val (y, s1) = morphism1(x)
          val (z, s2) = morphism2(y)
          (z, s1 + s2)
        }
      }

      def pure[A](x: A): Writer[A] = (x, "")
    }

    val upCase: String => Writer[String] = s => (s.toUpperCase, " My Logger: which 'upCases' the words")

    val toWords: String => Writer[List[String]] = s => (s.split(' ').toList, " and splits into a List of 'toWords' ")

    val mikeyStringFunction: String => Writer[String] = s => (s, " s._2 Add Mikey")

    val reverseMePlease: String => Writer[String] = s => (s, s" ${s.reverse} ")
  }

  import kleisli._

  val process: String => Writer[List[String]] = {
    upCase >=> toWords
  }

  val mikeyWackyStringFactory: String => Writer[String] = mikeyStringFunction >=> reverseMePlease


}