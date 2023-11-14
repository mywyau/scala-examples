package weaverTest

import org.scalacheck.Gen
import weaver._
import weaver.scalacheck._

// Notice the Checkers mix-in
object ForallExamples extends SimpleIOSuite with Checkers {

  // CheckConfig can be overridden at the test suite level
  override def checkConfig: CheckConfig =
    super.checkConfig.copy(perPropertyParallelism = 100)

  test("Gen form") {
    // Takes an explicit "Gen" instance. There is only a single
    // version of this overload. If you want to pass several Gen instances
    // at once, just compose them monadically.
    forall(Gen.posNum[Int]) { a =>
      expect(a > 0)
    }
  }

  test("Arbitrary form") {
    // Takes a number of implicit "Arbitrary" instances. There are 6 overloads
    // to pass 1 to 6 parameters.
    forall { (a1: Int, a2: Int, a3: Int) =>
      expect(a1 * a2 * a3 == a3 * a2 * a1)
    }
  }

  test("foobar") {
    // CheckConfig can be overridden locally
    forall.withConfig(super.checkConfig.copy(perPropertyParallelism = 1,
      initialSeed = Some(7L))) {
      (x: Int) => expect(x > 0)
    }
  }

}