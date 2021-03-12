package topics.implicits.Runner

import topics.implicits.{Conversions, Currency, Money, TaxRate}
import topics.implicits.MoneySyntax._

object ImplicitClassRunner extends App {

  val amount: Double = 30.5

//  amount.dollars shouldBe Money(amount, Currency.USD)
//  amount.euros shouldBe Money(amount, Currency.EUR)
//  amount.pounds shouldBe Money(amount, Currency.GBP)



}
