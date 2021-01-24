import Calculator._


class FlatSpecWithMatchersExampleTest extends SpecBase("CubeCalculator") {

  ".cube()" should "calculate the 'cube' of 3 to be 27" in {

    cube(3) shouldBe 27
  }

  ignore should "calculate the 'cube' of 4 to be 64" in {

    cube(4) shouldBe 64
  }

  it should "calculate the 'cube' of 5 to be 125" in {

    cube(5) shouldBe 125
  }

}
