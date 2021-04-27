package categoryTheoryAndCats.validated

import cats.Semigroupal
import cats.data.Validated
import cats.implicits._

case class VeryConvenient(i: Int, j: Int, k: String, l: Boolean)

class ValidatedExample2 {

  //Combining instances of Validated

  type AllErrorsOr[A] = Validated[String, A]

  /*  Validated accumulates errors using a Semigroup, so we need one of those
      in scope to summon the Semigroupal. If no Semigroup is visible at the call
      site, we get an annoyingly unhelpful compila􀦞on error:
      */

  //  Once we import a Semigroup for the error type, everything works as expected:

  import cats.instances.string._

  val semigroupalValidated: Semigroupal[AllErrorsOr] = Semigroupal[AllErrorsOr] //needs an implicit Semigroup for

  /*
    As long as the compiler has all the implicits in scope to summon a Semigroupal
    of the correct type, we can use apply syntax or any of the other
      Semigroupal methods to accumulate errors as we like:
  */

  import cats.syntax.apply._ // for tupled

  val errorOne: Validated[String, Int] = "Error 1".invalid[Int]
  val errorTwo: Validated[String, Int] = "Error 2".invalid[Int]

  val combineErrors: Validated[String, (Int, Int)] = (errorOne, errorTwo).tupled
  // res14: cats.data.Validated[String,(Int, Int)] = Invalid(Error 1
  // Error 2)

  //methods to use on Validated

  val mapTime100 = 123.valid.map(_ * 100)

  val leftMapAFunction = 123.invalid.leftMap(_.toString) // map the lhs

  val bifunctorMapBothStuff = 123.valid[String].bimap(
    fe = _ + "!",
    fa = _ * 100
  )

  val bimap2 = "?".invalid[Int].bimap(_ + "!", _ * 100)

  /*
    We can’t flatMap because Validated isn’t a monad. However, Cats does
    provide a stand-in for flatMap called andThen. The type signature of
    andThen is iden􀦞cal to that of flatMap, but it has a different name because
    it is not a lawful implementa􀦞on with respect to the monad laws:
  */

  // andThen

  32.valid andThen { a => // anonymous version of andThen Validated composition
    10.valid.map { b =>
      a + b
    }
  }

  val valid1: Validated[Nothing, Int] = 32.valid
  val valid2: Validated[Nothing, Int] = 10.valid

  val valid3: Validated[Nothing, Int] = valid1 andThen { a: Int => // named/non-anonymous version, similar to Monad flatmap composition using
    valid2.map { b => // andThen but called andThen since it would be unlawful to have flatMap
      a + b
    }
  }

  /*
    If we want to do more than just flatMap, we can convert back and forth between
    Validated and Either using the toEither and toValidated methods.
      Note that toValidated comes from [cats.syntax.either]:
  */

  import cats.syntax.either._ // for toValidated
  // import cats.syntax.either._

  val i1a: Validated[String, Int] = "Badness".invalid[Int]
  val i1b: Either[String, Int] = "Badness".invalid[Int].toEither
  val i1c: Validated[String, Int] = "Badness".invalid[Int].toEither.toValidated

  // similar to Either we can use the ensure method to fail with a specific error if a predicate does not hold

  val ensureMethod: Validated[String, Int] = 123.valid[String].ensure(onFailure = "Negative")(f = _ > 0)
  /*   ensure is curried with:
      1. the invalid lhs,
      2. the actual predicate it needs to satisfy
  */

  // Finally, we can call getOrElse or fold to extract values from the Valid and
  //  Invalid cases:

  val getOrElseExample = "fail".invalid[Int].getOrElse(0)

  // works differently to fold in native scala this is not curried and works on both sides to get the values
  val foldExample = "fail".invalid[Int].fold(fe = _ + "!!!", fa = _.toString)


}

object ValidatedExample2 extends App {

  val validatedExample2 = new ValidatedExample2
}
