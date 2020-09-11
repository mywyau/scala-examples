package Implicits.Runner

import Implicits.{Conversions, Currency, Money, TaxRate}
import Implicits.MoneySyntax._

object ImplicitClassRunner extends App {

  val amount: Double = 30.5

//  amount.dollars shouldBe Money(amount, Currency.USD)
//  amount.euros shouldBe Money(amount, Currency.EUR)
//  amount.pounds shouldBe Money(amount, Currency.GBP)



}
