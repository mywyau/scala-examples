package learnCatsAndStuff.functors

import cats._
import cats.implicits._

case class Money(amount: Int)

case class Salary(size: Money)

class ContravariantFunctors {

  implicit val showMoney: Show[Money] = Show.show(m => s"$$${m.amount}")

  implicit val showSalary: Show[Salary] = showMoney.contramap(_.size) //used contramap

  val compare2Nums: Int = Ordering.Int.compare(2, 1)

  val compareNums: Int = Ordering.Int.compare(1, 2)

  import scala.math.Ordered._

  implicit val moneyOrdering: Ordering[Money] = Ordering.by(_.amount)

  val check100LT200 = Money(100) < Money(200)

  


}
