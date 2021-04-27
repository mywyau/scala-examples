package learnCatsAndStuff.validated.liefBattermann

import cats.implicits._

object ApTieFighterExample extends App {

  def add(a: Int, b: Int) = a + b

  val etaExpandedAdd: (Int, Int) => Int = add _
  //creating a function out of a method, via eta expansion. you can partially apply and still perform eta expansion to produce a partially applied function from a method

  val curriedAdd: Int => Int => Int = etaExpandedAdd.curried // still pretty much the same function as the two above it just curried.

  // before the first <*> the add function is lifted into an Option so becomes Option[Int => Int => Int]
  val add2AndSomethingElse: Option[Int => Int] = (add _).curried.pure[Option] <*> Option(2) //<*> Option(5) //remember pure lifts the function into the desired context. In this case Option
  // .curried converts the (Int, Int) to a (Int) => (Int) which is then used to transform add function from '(Int, Int) => Int' to 'Int => Int => Int'

  // <*>  - this is apply/ap from weaker Apply in cats, (similar to / part of Applicative functor), syntax sometimes aka 'tie-fighter' from Star Wars

  println(add2AndSomethingElse)
}
