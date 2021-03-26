package learnCatsAndStuff.semigroupal

import cats.instances.option._
import cats.syntax.apply._ // for tupled and mapN

class Apply1 {

  /*  Apply Syntax
      Cats provides a convenient apply syntax that provides a shorthand for the
      semigroupal methods. We import the syntax from cats.syntax.apply.

      Here’s an example:

      import cats.instances.option._ // for Semigroupal
      import cats.syntax.apply._ // for tupled and mapN
    */

  // allowed from 1 to 22

  val tupled1: Option[(Int, String)] = (Option(123), Option("abc")).tupled

  val tupled2: Option[(Int, String, Boolean)] = (Option(123), Option("abc"), Option(true)).tupled


/*  In addition to tupled, Cats’ apply syntax provides a method called mapN that
    accepts an implicit Functor and a function of the correct arity to combine the
    values:*/

  case class Cat(name: String, born: Int, color: String)

  val catify: Option[Cat] = (Option("Garfield"), Option(1978), Option("Orange & Black")).mapN(Cat.apply)
  // res9: Option[Cat] = Some(Cat(Garfield,1978,Orange & black))

/*  Internally mapN uses the Semigroupal to extract the values from the Option
    and the Functor to apply the values to the function.*/

  // if you supply the wrong amount of parameters or the wrong values it can blow up

  val add: (Int, Int) => Int = (a, b) => a + b

//  val killMe = (Option(1), Option(2), Option(3)).mapN(add)   -- will not compile

//  (Option("cats"), Option(true)).mapN(add)                    -- will not compile
}
