package Implicits.Runner

import Implicits.{Conversions, TaxRate}

object Runner extends App {

  val conversion = new Conversions
  implicit val sales_tax: TaxRate = TaxRate(0.5)

  val t = List(conversion.withTax(1000), conversion.withTax2(1000)).map(i => println(i))

}
