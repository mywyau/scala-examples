package rockTheJVM.abstractMath

object Monoids {

  import cats.syntax.semigroup._

  val numbers = (1 to 1000).toList
  val strings = List("a", "bc", "def", "ghij")

  // |+| is always associative

  val sumLeft = numbers.foldLeft(0)(_ |+| _)
  val sumRight = numbers.foldRight(0)(_ |+| _)

  //  def combineFold[T](list: List[T])(implicit semigroup: Semigroup[T]) =    // notice how for the seed value we need a generic empty for possibly Int, String, Custom type
  //    list.foldLeft()(_ |+| _)                                                // Hence the concept of a Monoid - we need an empty, zero, starting/neutral value


  import cats.Monoid

  val intMonoid = Monoid[Int]
  val combineInt = intMonoid.combine(23, 999)
  val zero = intMonoid.empty

  val stringMonoid = Monoid[String]
  val emptyString = stringMonoid.empty
  val combineString = stringMonoid.combine("I", "understand monoids")

  import cats.instances.option._
  //  import cats.syntax.monoid._   // think we cannot have both semigroup and monoid syntax in scope

  val combinedOptionFancy = Option(3) |+| Option(7) //Some(10)

  val emptyOption = Monoid[Option[Int]].empty
  val combineOption = Monoid[Option[Int]].combine(Option(2), Option.empty[Int]) // Some(2)


  val phonebook =
    List(
      Map(
        "Alice" -> 235,
        "Bob" -> 467
      ),
      Map(
        "Charlie" -> 372,
        "Daniel" -> 889
      ),
      Map(
        "Tina" -> 372
      )
    )

  // My implementation - apparently there was no need to define a Monoid for map as Cats already has an instance for it
  //  implicit val phoneBookImplicit =
  //    new Monoid[Map[String, Int]] {
  //
  //      override def empty: Map[String, Int] =
  //        Map.empty
  //
  //      override def combine(x: Map[String, Int], y: Map[String, Int]): Map[String, Int] =
  //        (x ++ y)
  //    }


  // answer in video

  import cats.instances.map._

  def combineFold[T](list: List[T])(implicit monoid: Monoid[T]) =
    list.foldLeft(monoid.empty)(_ |+| _)

  case class ShoppingCart(items: List[String], total: BigDecimal)

  implicit val shoppingMonoid: Monoid[ShoppingCart] = // could define a new trait such as new Monoid[] and implement the empty and combine methods
    Monoid.instance[ShoppingCart](
      ShoppingCart(List(), 0.0),
      (sa, sb) => ShoppingCart(sa.items ++ sb.items, sa.total + sb.total)
    )

  def checkout(shoppingCarts: List[ShoppingCart]) = combineFold(shoppingCarts)

  def main(args: Array[String]): Unit = {

    // Part 1

    //    println(sumLeft)
    //    println(sumRight)

    //    println(s"using the new generic .combineFold() we created on Ints: ${combineFold(numbers)}")
    //    println(s"using the new generic .combineFold() we created on Strings: ${combineFold(strings)}")


    // Part 2 combining Maps[String, Int] answer from video
    val massivePhonebook = combineFold(phonebook)
    //    println(s"using the new generic .combineFold() we created on Map[String, Int]: ${massivePhonebook}")


    // Part 3

    val singleCustomerWithMultipleShoppingCarts: List[ShoppingCart] =
      List(
        ShoppingCart(List("tv", "iphone", "bread"), 20000),
        ShoppingCart(List("apple", "pears"), 799),
        ShoppingCart(List.empty, 0),
      )

    println("shopping exercises - Monoid custom type")
    println(checkout(singleCustomerWithMultipleShoppingCarts))
  }
}
