package coursera.week1

import specBase.SpecBase

class PascalsTriangleFlatSpecMockito extends SpecBase {

  val exerciseOne = new PascalsTriangle

  behavior of "PascalsTriangle"

  ".notTailRecFactorial(n)" should "calculate the factorial of a number, n" in {

    exerciseOne.notTailRecFactorial(5) mustBe 120
  }

  ".factorial(n)" should "calculate the factorial of a number, n" in {

    exerciseOne.factorial(10) mustBe 3628800
  }

  //  ".pascal(column,row)" should "calculate the correct number in the triangle" in {
  //
  //    weekOne.pascal(2, 5) mustBe 6
  //  }

  ".pascal2(column,row)" should "given the row and column calculate the correct number within the triangle" in {

    exerciseOne.pascal2(1, 1) mustBe 1
  }

}
