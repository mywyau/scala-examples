package topics.Implicits

class ImplicitClasses {

  val amount: Double = 30.5

//  amount.dollars shouldBe Money(amount, Currency.USD)
//  amount.euros shouldBe Money(amount, Currency.EUR)
//  amount.pounds shouldBe Money(amount, Currency.GBP)
//
//





}


sealed trait Currency
object Currency {
  case object EUR extends Currency
  case object USD extends Currency
  case object GBP extends Currency
}

case class Money(amount: Double, currency: Currency)

// In the above Topics.example, we’ve created an implicit class called RichMoney that adds euros, dollars, and pounds methods for any Double.
// It also extends AnyVal to avoid runtime allocation.

object MoneySyntax {
  implicit class RichMoney(val amount: Double) extends AnyVal {
    def euros: Money = Money(amount, Currency.EUR)
    def dollars: Money = Money(amount, Currency.USD)
    def pounds: Money = Money(amount, Currency.GBP)
  }
}
