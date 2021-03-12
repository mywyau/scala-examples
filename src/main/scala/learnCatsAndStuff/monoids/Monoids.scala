package scala.learnCatsAndStuff.monoids

import cats.instances.all._
import cats.syntax.semigroup._
import cats.{Monoid, Semigroup} // for Monoid

class Monoids {

  /*

    Monoid is a category which extends Semigroup  in Cats  -- category theory would say something like a Monoid is also a Semigroup

    In practice we only need to think about laws when we are writing our own
    Monoid instances. Unlawful instances are dangerous because they can yield
    unpredictable results when used with the rest of Cats’ machinery. Most of
    the timme we can rely on the instances provided by Cats and assume the library
    authors know what they’re doing.

    Semigroup has a combine whilst Monoid has combine and empty these properties along with composition and associativity
  */

  val semigroupCombine: String = Semigroup[String].combine("Mike", "y")

  val combineString: String = Monoid[String].combine("Hi", "There")

  val emptyString: String = Monoid[String].empty

  val combineInts: Int = Monoid[Int].combine(9000, 1)

  val optA: Option[Int] = Option(22)

  val optB: Option[Int] = Option(20)

  val monoidCombineOpt: Option[Int] = Monoid[Option[Int]].combine(optA, optB) //needs an implicit for Options

  /* |+| - Semigroup/Monoid combine
    Cats provides syntax for the 'combine' method in the form of the |+| operator.
    Because combine technically comes from Semigroup, we access the syntax
    by importing from cats.syntax.semigroup:
  */

  val intResult: Int = 1 |+| 2 |+| Monoid[Int].empty //i.e for Int, empty would be zero

  def poopyAdder(items: List[Int]): Int = {
    items.foldLeft(0)(_ + _) //old boring adder of List[Int] using foldleft
  }

  def superAdder(items: List[Int]): Int = {
    items.foldLeft(Monoid[Int].empty)(_ |+| _) //fancy adder of List[Int] using foldleft
  }

  /*
    We have a List[Int] implementation for superAdder
  */

  def superOptionAdder(items: List[Option[Int]]): Int = {
    items.flatten.foldLeft(0)(_ + _) //old way adder of List[Option[Int]] squish the Seq[Option[_]] using flatten then fold the Ints to sum it up.
  }

  def freshAllInOneAdder[A](items: List[A])(implicit monoid: Monoid[A]): A = {
    items.foldLeft(Monoid[A].empty)(_ |+| _) //fancy new adder which works with Lists[A], List[Option[A]]
  }

  // now using .freshAllInOneAdder() and no altering the code make a implicit type class instance for 'case class Order(totalCost: Double, quantity: Double)' Order type

  case class Order(totalCost: Double, quantity: Double)

  implicit val monoidOrder: Monoid[Order] = new Monoid[Order] {

    override def combine(order1: Order, order2: Order): Order = {
      Order(
        totalCost = order1.totalCost + order2.totalCost,
        quantity = order1.quantity + order2.quantity
      )
    }

    override def empty: Order = Order(totalCost = 0, quantity = 0)
  }

  val orderOne = Order(totalCost = 1000, quantity = 10)
  val orderTwo = Order(totalCost = 2000, quantity = 5)

  val orderMonoidCombine = Monoid[Order].combine(orderOne, orderTwo)
  val orderMonoidEmpty = Monoid[Order].empty

  val opt3: Option[Int] = Option(1) |+| Option(2)

  val tupleOne: (Int, String) = (1, "abc")
  val tupleTwo: (Int, String) = (5, "defg")

  val oldComboTuple: (Int, String) = (tupleOne._1 + tupleTwo._1, tupleTwo._1 + tupleTwo._2) // Yuck!! easy but little gross imagine combining 7 tuples together
  val comboTuple: (Int, String) = tupleOne |+| tupleTwo // fancy tuple combine

  //  We can also write generic code that works with any type for which we have an instance of Monoid:

  def addAll[A](values: List[A])(implicit monoid: Monoid[A]): A =
    values.foldRight(monoid.empty)(_ |+| _) // guess foldLeft or Right works here tbh since associativity

  val addListOfNums: Int = addAll(List(1, 2, 3))

  val sumListOptInts: Option[Int] = addAll(List(None, Some(2), Some(3)))
}