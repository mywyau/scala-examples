package week1


import coursera.week1.WeekOne
import specBase.SpecBase

class WeekOneSpec extends SpecBase {

  val weekOne = new WeekOne

  behavior of "WeekOne"

  ".notTailRecFactorial(n)" should "calculate the factorial of a number, n" in {

    weekOne.notTailRecFactorial(5) shouldBe 120
  }

  ".factorial(n)" should "calculate the factorial of a number, n" in {

    weekOne.factorial(10) shouldBe 3628800
  }

//  ".pascal(column,row)" should "calculate the correct number in the triangle" in {
//
//    weekOne.pascal(2, 5) shouldBe 6
//  }

//  ".pascal2(column,row)" should "calculate the correct number in the triangle" in {
//
//    weekOne.pascal2(5, 2) shouldBe 6
//  }

  ".pascal3(column,row)" should "calculate the correct number in the triangle" in {

    weekOne.pascal3(1, 1) shouldBe 1
  }

//  ".pascalT(column,row)" should "calculate the correct number in the triangle" in {
//
//    weekOne.pascalT(2, 5) shouldBe 7
//  }
}
