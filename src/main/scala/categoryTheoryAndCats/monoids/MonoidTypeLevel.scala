package categoryTheoryAndCats.monoids


import cats.Monoid
import cats.implicits._

case class Money(pound: Int, penny: Int)

class MonoidTypeLevel {

  //  Folding Lists with Monoids

  implicit val moneyMonoid = new Monoid[Money] {

    override def empty: Money = Money(0, 0)

    override def combine(x: Money, y: Money): Money = Money(x.pound + y.pound, x.penny + y.penny)
  }

  ///Money example

  val lastYearExpenses = List(Money(3, 4), Money(34, 5), Money(12, 0))

  def totalExpenses(expenses: List[Money])(m: Monoid[Money]): Money = {
    expenses.foldLeft(m.empty) {
      case (acc, money) => m.combine(acc, money)
    }
  }
}

case class TwitterUser(userName: String, followers: Int) extends Ordered[TwitterUser] {

  override def compare(that: TwitterUser): Int = {
    val c = this.followers - that.followers

    if (c == 0) this.userName.compareTo(that.userName) else c
  }
}


object TwitterMonoid extends App {

  implicit val twitterMonoid: Monoid[TwitterUser] = new Monoid[TwitterUser] {   //help define a type class instance

    override def empty: TwitterUser =
      TwitterUser("MinUser", Int.MinValue)

    override def combine(x: TwitterUser, y: TwitterUser): TwitterUser =
      if (x.compareTo(y) >= 1) x else y
  }

  val harmeetsingh = TwitterUser("singh_harmeet13", 132)
  val knoldus = TwitterUser("knolspeak", 575)
  val vikas = TwitterUser("vhazrati", 387)
  val dzone = TwitterUser("dzone", 10640)
  val scala = TwitterUser("scala_lang", 20421)

  case class Max(user: TwitterUser) {

    //definied an operator for case class Max type
    def +(usr: Max)(implicit monoid: Monoid[TwitterUser]): Max = {
      Max(monoid.combine(this.user, usr.user))
    }
  }

  val winner: Max = Max(harmeetsingh) + Max(knoldus) + Max(vikas) + Max(dzone) + Max(scala)

  println(s"Winner Is: ${winner.user.userName} with ${winner.user.followers} users")
  assert(winner.user == scala)
}

object MapMonoid extends App {

  //  implicit val mapMonoid: Monoid[String] = new Monoid[String] {
  //
  //    override def empty: String = ""
  //
  //    override def combine(x: String, y: String): String = x + y
  //  }

  //  implicit def mikeyMapHacks[K, V]()(implicit mapMonoid: Monoid[V]): Monoid[Map[K, V]] =
  //    new Monoid[Map[K, V]] {
  //      override final def combine(map1: Map[K, V], map2: Map[K, V]): Map[K, V] =
  //        (map1.keySet | map2.keySet).foldLeft(this.empty) {
  //          case (acc, key) =>
  //            acc + (key ->
  //              mapMonoid.combine(
  //                map1.getOrElse(key, default = mapMonoid.empty),
  //                map2.getOrElse(key, default = mapMonoid.empty)
  //              ))
  //        }
  //
  //      override final def empty: Map[K, V] = Map.empty
  //    }

  val map1: Map[String, Int] = Map("a" -> 1, "b" -> 2)
  val map2: Map[String, Int] = Map("a" -> 10, "b" -> 5)

  val map3: Map[String, Int] = map1 |+| map2
  println(map3)

  val map4: Map[Int, String] = Map(1 -> "a", 2 -> "b")
  val map5: Map[Int, String] = Map(1 -> "a", 2 -> "b")
  val map5b: Map[Int, String] = Map(5 -> "a", 10 -> "b")

  val map6: Map[Int, String] = map4 |+| map5
  val map7: Map[Int, String] = map5 |+| map5b
  val map8: Map[Int, String] = map5 |+| map5b |+| map4
  println("map6 " + map6)
  println("map7 " + map7)
  println("map8 " + map8)

}
