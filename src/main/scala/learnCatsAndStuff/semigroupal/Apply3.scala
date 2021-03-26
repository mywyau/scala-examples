package learnCatsAndStuff.semigroupal

import cats.Semigroupal
import cats.instances.future._ // for semigroupal
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.higherKinds

class Apply3 {

  /*  Semigroupal doesn’t always provide the behaviour we expect, particularly
    for types that also have instances of Monad. We have seen the behaviour of
      the Semigroupal for Option. Let’s look at some examples for other types.
      */

  val futurePair: Future[(String, Int)] = Semigroupal[Future].product(Future("Hello"), Future(123))

  val waitForResult = Await.result(futurePair, 1.second)

  /*
    The two Futures start execu􀦞ng the moment we create them, so they are
    already calcula􀦞ng results by the 􀦞me we call product. We can use apply
    syntax to zip fixed numbers of Futures:
  */

  import cats.syntax.apply._ // for mapN

  case class Cat(name: String, yearOfBirth: Int, favoriteFoods: List[String])

  val futureCat =
    (Future("Garfield"), Future(1978), Future(List("Lasagne")))
      .mapN(Cat.apply)

  val waitForFutureCat = Await.result(futureCat, 1.second)

  /*  List
      Combining Lists with Semigroupal produces some poten􀦞ally unexpected
      results. We might expect code like the following to zip the lists, but we actually
      get the cartesian product of their elements:
  */

  import cats.Semigroupal
  import cats.instances.list._ // for Semigroupal

  val cartesianProduct: Seq[(Int, Int)] = Semigroupal[List].product(List(1, 2), List(3, 4))
  // res5: List[(Int, Int)] = List((1,3), (1,4), (2,3), (2,4))
  //we get something different and not a zipped list. We thought we would get List((1,3), (2,4))


  /*  Either
      We opened this chapter with a discussion of fail-fast versus accumula􀦞ng
      error-handling. We might expect product applied to Either to accumulate
      errors instead of fail fast. Again, perhaps surprisingly, we find that product
      implements the same fail-fast behaviour as flatMap:
 */

  import cats.instances.either._ //for Semigroupal

  type ErrorOr[A] = Either[Vector[String], A]

  val someErrors = Semigroupal[ErrorOr].product(
    Left(Vector("Error 1")),
    Left(Vector("Error 2"))
  )

  /*
    res7: ErrorOr[(Nothing, Nothing)] = Left(Vector(Error 1))   -- dies on the first error left even tho it can evaluate and see the second error
    this means Eithers fails fast and type bombs out I think.

    In this example product sees the first failure and stops, even though it is possible
    to examine the second parameter and see that it is also a failure.
    */




}
