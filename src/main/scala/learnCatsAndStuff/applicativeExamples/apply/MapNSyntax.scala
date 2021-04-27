package learnCatsAndStuff.applicativeExamples.apply

import cats.Applicative
import cats.implicits._

case class MikeyMikey(i: Int, j: Int)

case class SikeMike(i: Int, s: String, b: Boolean)

object MapNSyntax {

  val optThree = Option(3)
  val optFour = Option(4)
  val optTen = Option(10)

  def mikeyMethod(i: Int, j: Int, k: Int): Option[MikeyMikey] = //some dumb method that takes all three optional values
    Some(MikeyMikey(i, j)) // creates a dummy case class the effect is it takes three values but for some dumb reason ignores 'k' to create a Option[MikeyMikey]

  val notFunSyntax: Option[Option[MikeyMikey]] = Applicative[Option].map3(optThree, optFour, optTen)(mikeyMethod)
  // mikeyMethod evaluated via eta expansion which produces a function with signature
  // (i,j,k) => MikeyMikey
  // the three values are kinda tupled which then has the '(i,j,k) => MikeyMikey' function applied to it

  // .mapN()
  val easyModeSyntax: Option[Option[MikeyMikey]] = // nested Option since .mikeyMethod() wraps the value in 'Some' for some random reason
    (optThree, optFour, optTen).mapN(mikeyMethod) // works on a tuple of contextual values (F[A], F[B]. F[C]).mapN{ (a:A, b:B, c:C) => Z }
  // We don’t have to mention the type or specify the number of values we’re composing together, so there’s a little less boilerplate here.

  // Another convoluted example

  val oInty = Option(8)
  val oStringy = Option("Hello World")
  val oBooly = Option(true)

  def sikeMikeHandler(i: Int, s: String, b: Boolean): SikeMike = { //adding some logic to be dumb
    (i, s, b) match {
      case namedTuple@(10, "Mikey", false) => SikeMike(namedTuple._1, namedTuple._2, namedTuple._3)
      case namedTuple@(i, "Hello World", true) if i == 8 => SikeMike(i + 7, namedTuple._2, namedTuple._3)
      case _ => SikeMike(0, "Uh oh!", true)
    }
  }

  val mapNSikeMike: Option[SikeMike] = (oInty, oStringy, oBooly).mapN(sikeMikeHandler)

  // traverse example of mapN, to handle a List of inputs to then
}

object SyntaxRunner extends App {

  println(MapNSyntax.notFunSyntax + " this is the sweaty syntax\n")
  println(MapNSyntax.easyModeSyntax + " this is easy mode mapN syntax\n")

  println(MapNSyntax.mapNSikeMike + " this is another example of mapN syntax\n")

}
