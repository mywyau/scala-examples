package calculator

import calculator.Calculator.cube
import specBase.SpecBase

class CalculatorFlatSpecMockito extends SpecBase {

  // needs the 'behavior of $component' needed to begin a TestSpec for nicer wording with heading in terminal
  behavior of "CubeCalculator" // Heading of the test spec can be specified here.

  // tests can then go below

  ".cube()" should "calculate the 'cube' of 3 to be 27" in {


    cube(3) mustBe 27
  }

  ignore should "calculate the 'cube' of 4 to be 64" in {

    cube(4) mustBe 64
  }

  it should "calculate the 'cube' of 5 to be 125" in {

    cube(5) mustBe 125
  }

  behavior of "Mikey" // Heading of the test spec can be specified here.


}
