package catsnotes.applicativeExamples.apply

import cats.Apply
import cats.implicits._

object NativeVsCats {

  // Apply basically takes a function in a computational context and then is able to apply it to a value wrapped in a computation context
  //e.g.

  //Example 1 Native Scala

  /*
    We have a F[A => B] and a F[A] to be able to apply the function within the F[A => B] to the value in F[A] we will have to do some map/flatMap gymnastics.
   */

  val optAddTwoFunction: Option[Int => Int] = Some(i => i + 2) // adds two to a given number duh

  val optTen: Option[Int] = Option(10) // Optional ten

  val nativeApplyFunction: Option[Int] = optTen.flatMap(ten => optAddTwoFunction.map(func => func(ten))) // native apply not too bad but bit un-wieldy

  // Incomes Apply typeclass with it's ap function lol

  val addSeven: Int => Int = i => i + 7 //meet a new totally original function, add seven

  val liftedFunctionOne: Option[Int => Int] = Option(addSeven) // we do the same thing of lifting the fucntion into a computational context

  val liftedFunctionThree: Option[(Int, Int) => Int] = Option((i: Int, j: Int) => i + j + 100) // another lifted anonymous function just to hammer home the point

  val optHundred: Option[Int] = Option(100) // your functor value

  // we will now finally use the Cats library

  val applyLiftedFunctionToFunctorValue: Option[Int] = Apply[Option].ap(liftedFunctionOne)(optHundred) // should equal Some(107) or Option(107)
  // this should just add 7 to the 100 within Option(100) -- i.e Option(100 + 7)  or Option( 100 => 100 + 7 ) = Option(107)

}

object ApplyRunner extends App {

  println(NativeVsCats.nativeApplyFunction + " this value is Native handling")
  println(NativeVsCats.applyLiftedFunctionToFunctorValue + " this value is using the Cats Apply ap function")

}
