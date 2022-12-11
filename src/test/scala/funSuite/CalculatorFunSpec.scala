package funSuite

import calculator.Calculator
import org.scalatest.funsuite.AnyFunSuite

class CalculatorFunSpec extends AnyFunSuite {

  test("CubeCalculator.cube(3) should return 27") {
    assert(Calculator.cube(3) === 27)
  }

//  test("CubeCalculator.addTwo(10) should return 12") {
//    assert(Calculator.addTwo(10) === 15)
//  }
}
