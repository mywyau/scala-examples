import org.scalatest.FunSuite

class CalculatorSpec extends FunSuite {

  test("CubeCalculator.cube(3) should return 27") {
    assert(Calculator.cube(3) === 27)
  }
}
