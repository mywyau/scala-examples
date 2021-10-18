package rockTheJVM.abstractMath

object Semigroups {

  // Semigroups comibine elements of the same type

  import cats.Semigroup
  import cats.instances.int._ // explicit imports for scala cats, could just import everything

  val naturalIntSemigroup = Semigroup[Int] // so basically addition
  val intCombination = naturalIntSemigroup.combine(2, 46)

  import cats.instances.string._

  val naturalStringSemigroup = Semigroup[String] // so basically concatenation
  val stringCombination = naturalStringSemigroup.combine("I love", "Cats")

  def reduceInts(list: List[Int]): Int = list.reduce(naturalIntSemigroup.combine)

  def reduceStrings(list: List[String]): String = list.reduce(naturalStringSemigroup.combine)

  // define general API

  def reduceThings[T](list: List[T])(implicit semigroup: Semigroup[T]): T = list.reduce(semigroup.combine)
  // defined a general reduction method for any type - Woah! we didn't need to define a version for each type we could just delete them and replace them both with this


  def main(args: Array[String]): Unit = {

    println(intCombination)
    println(stringCombination)

    val numbers = (1 to 10).toList
    println(reduceInts(numbers))

    val strings = List("I'm", "starting", "to", "like", "Semigroups")
    println(reduceStrings(strings))

    // our general API
    println(reduceThings(numbers))
    println(reduceThings(strings))

    import cats.instances.option._ // semigroup for option in scope
    // compiler will produce an implicit Semigroup[Option[Int]] - combine will produce another option with the summed element, well only if they are both defined
    // compiler will produce an implicit Semigroup[Option[String]] - combine will produce another option with the concatenated elements, again only if they are both defined

    val numberOptions: List[Option[Int]] = numbers.map(n => Option(n))
    val stringOptions: List[Option[String]] = strings.map(s => Option(s))

    println(reduceThings(numberOptions)) //general reduce even works on Lists containing Option[Int]
    println(reduceThings(stringOptions)) //general reduce even works Lists containing Option[String]

//    import cats.syntax.semigroup._
//    println(Option(7) |+| None) //Some(7)
//    println(Option(7) |+| Option(8)) //Some(15)


    // Exercise my implementation

    case class Expense(id: Long, amount: Double)

    //    implicit val expenseSemigroup = new Semigroup[Expense] {
    //      override def combine(x: Expense, y: Expense): Expense = {
    //        if (x.id == y.id) {
    //          Expense(x.id, x.amount + y.amount)
    //        } else {
    //          throw new Exception("Unable to combine expenses, expenses must be of the same id")
    ////          Expense(0, 0)
    //        }
    //      }
    //    }
    //
    //    val expense1a = Expense(1, 100)
    //    val expense1b = Expense(1, 200)
    //    val expense2 = Expense(2, 200)
    //
    //    println(expense1a |+| expense1b)
    //    println(expense2 |+| expense1b)


    // Answer


    implicit val expenseSemigroup =
      Semigroup.instance[Expense] { (e1, e2) => Expense(Math.max(e1.id, e2.id), e1.amount + e2.amount) }
    // we are able to use the .instance[Our Custom Type] instead of overriding the combine method

    val expense1a = Expense(1, 100)
    val expense1b = Expense(1, 200)
    val expense2 = Expense(2, 200)

    // test ex1

    val expenses = List(expense1a, expense1b)

    println(s"the total of the list of expenses is ${reduceThings(expenses)}")

    // semigroup extension methods - |+|

    import cats.syntax.semigroup._

    val combineExpensesSyntax = expense1a |+| expense1b // requires the presence of an implicit Semigroup[Expense]
    val anIntSum = 2 |+| 3 // requires the presence of an implicit Semigroup[Int]

    //TODO 2:

    def reduceThings2[T](list: List[T])(implicit semigroup: Semigroup[T]): T =
      list.reduce(_ |+| _)

//    def reduceThings3[T: Semigroup](list: List[T]): T =   // this is the alt way of using a type context on the type T so we can remove 'implicit semigroup: Semigroup[T]'
//      list.reduce(_ |+| _)

    println(s"the total of the list of expenses is ${reduceThings2(expenses)} using .reduceThings2 so the answers should match")

  }
}
