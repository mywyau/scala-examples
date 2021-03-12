package week1


import coursera.week1.PascalsTriangle
import specBase.SpecBase

class PascalsTriangleSpec extends SpecBase {

  val exerciseOne = new PascalsTriangle

  behavior of "PascalsTriangle"

  ".notTailRecFactorial(n)" should "calculate the factorial of a number, n" in {

    exerciseOne.notTailRecFactorial(5) shouldBe 120
  }

  ".factorial(n)" should "calculate the factorial of a number, n" in {

    exerciseOne.factorial(10) shouldBe 3628800
  }

  //  ".pascal(column,row)" should "calculate the correct number in the triangle" in {
  //
  //    weekOne.pascal(2, 5) shouldBe 6
  //  }

  ".pascal2(column,row)" should "given the row and column calculate the correct number within the triangle" in {

    exerciseOne.pascal2(1, 1) shouldBe 1
  }

}
