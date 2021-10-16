package funSuite

import org.scalatest._
import org.scalatest.funsuite.AnyFunSuite

class HelloSpec extends AnyFunSuite {

  test("Hello should start with H") {
    assert("Hello".startsWith("H"))
  }
}