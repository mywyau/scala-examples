package catsStuff.liefBattermann

import cats.implicits._

object ApTieFighterExampleNoComments extends App {

  def add(a: Int, b: Int) = a + b

  val etaExpandedAdd: (Int, Int) => Int = add _

  val curriedAdd: Int => Int => Int = etaExpandedAdd.curried

  val add2AndSomethingElse: Option[Int => Int] = (add _).curried.pure[Option] <*> Option(2)

  val customer2ID = CustomerID("Nico")
  val CustomerID(name) = customer2ID

  case class User(name: String, age: Int)

  val userBase =
    List(
      User("Travis", 28),
      User("Kelly", 33),
      User("Jennifer", 44),
      User("Dennis", 23)
    )

  val twentySomethings =
    for (user <- userBase if user.age >= 20 && user.age < 30)
      yield user.name

  twentySomethings.foreach(println)

  userBase.filter(user => user.age >= 20 && user.age < 30).map(_.name).foreach(println)
}
