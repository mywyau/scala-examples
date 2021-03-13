package topics.implicits

import scala.language.implicitConversions

class Conversions {

  implicit def list2ordered[A](x: List[A])(implicit elem2ordered: A => Ordered[A]): Ordered[List[A]] = {
    new Ordered[List[A]] {
      def compare(that: List[A]) = 1
    }
  }


  def withTax(price: BigDecimal)(implicit tax: TaxRate) = {
    val tax = TaxRate(1.6666)
    price * tax.rate
  }

  def withTax2(price: BigDecimal)(implicit tax: TaxRate) = {
    price * tax.rate
  }

}

case class TaxRate(rate: BigDecimal)
