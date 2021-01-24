package funSuite

import calculator.Calculator
import org.scalatest.FunSuite

class CalculatorFunSpec extends FunSuite {

  test("CubeCalculator.cube(3) should return 27") {
    assert(Calculator.cube(3) === 27)
  }
}
