package Topics.Polymorphism.Runner

import Topics.Polymorphism.Polymorph

object Runner extends App {

  val polymorpher = new Polymorph

  val discounts = polymorpher.applyDiscount(50)
  val specificTypes = polymorpher.applyDiscount[String]("Coupon-586")

  val coupon = s"Result of polymorpher.returnGenericApplyDiscount with String parameter = ${polymorpher.returnGenericApplyDiscount[String]("COUPON_123")}"
  val priceDiscount = s"Result of polymorpher.returnGenericApplyDiscount with Double parameter = ${polymorpher.returnGenericApplyDiscount[Double](10.5)}"
  val unknownDiscount = s"Result of polymorpher.returnGenericApplyDiscount with Char parameter = ${polymorpher.returnGenericApplyDiscount[Char]('U') }"

  val t = List(
    discounts,
    specificTypes,
    coupon,
    priceDiscount,
    unknownDiscount
  ).map(i => println(i))





}
