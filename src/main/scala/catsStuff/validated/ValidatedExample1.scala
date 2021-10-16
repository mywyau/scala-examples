package catsStuff.validated

import cats.Semigroupal
import cats.data.Validated
import cats.instances.list._ // for Monoid

class Validated1 {

  type AllErrorsOr[A] = Validated[List[String], A]

  val accumulateErrors: AllErrorsOr[(Nothing, Nothing)] = // hmm not sure why the type contains (Nothing, Nothing)
    Semigroupal[AllErrorsOr].product(
      Validated.invalid(List("Error 1")),
      Validated.invalid(List("Error 2"))
    )

  /*
    Validated complements Either nicely. Between the two we have support
    for both of the common types of error handling: fail-fast and accumula􀦞ng.

    There are multiple ways to create instances of Validated we will make a note of them
  */

  // using their built in .apply()  --- method

  val v: Validated.Valid[Int] = Validated.Valid(123)        // the type is less ideal since it is more narrow when using .apply()
  val i: Validated.Invalid[Int] = Validated.Invalid(123)    // the type is less ideal since it is more narrow when using .apply()

  // using the smart constructors of Validated

  val v2: Validated[List[String], Int] = Validated.valid[List[String], Int](123)            // using smart constructors widens the return type to be of Validated[+E, +A]
  val i2: Validated[List[String], Int] = Validated.invalid[List[String], Int](List("Badness"))   // this is probably more useful/workable as a type

  // extension methods

  import cats.syntax.validated._ // for valid and invalid extension syntax

  val v3: Validated[List[String], Int] = 123.valid[List[String]]
  val i3: Validated[List[String], Int] = List("Badness").invalid[Int]

  // using pure and raiseError

  import cats.syntax.applicative._
  import cats.syntax.applicativeError._    // for raiseError

  type ErrorsOr[A] = Validated[List[String], A]

  val v4 = 123.pure[ErrorsOr]     //giving pure the type helps it figure out what type to lift "123" into, in this case it's Validated[List[String], A]

  val i4 = List("Badness").raiseError[ErrorsOr, Int]
  //giving raiseError the types helps it figure out what type to lift 'List("Badness")' into, in this case it's Validated[List[String], A]

  /*
    Finally, there are helper methods to create instances of Validated from different
    sources. We can create them from Exceptions, as well as instances of
    Try, Either, and Option:
  */

  val i5a: Validated[NumberFormatException, Int] = Validated.catchOnly[NumberFormatException]("foo".toInt)
    //so when a NumberformatException occurs it will return the LHS / Invalid which contains the NumberFormatException kinda like Either

  val i5b: Validated[Throwable, Int] = Validated.catchOnly[Throwable](sys.error("Badness"))

  val i5c: Validated[Throwable, Int] = Validated.fromTry(scala.util.Try("foo".toInt))
  // convert to Validated from a Try

  val i5d: Validated[String, Int] = Validated.fromEither[String, Int](Left("Badness"))
  // convert to Validated from a Either

  val i5e: Validated[String, Int] = Validated.fromOption[String, Int](o = None, ifNone = "Badness")
  // from a Option but you need to define the Invalid case for when it is None so, we said for parameter 'o = None' handle it using the 'ifNone = "Badness"'
  // hence the i5e returns Invalid("Badness")

}

object ValidatedRunner extends App {

  val validated1 = new Validated1

  println(validated1.accumulateErrors)


}
