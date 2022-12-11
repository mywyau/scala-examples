package calculator

object Calculator extends App {

  def cube(x: Int): Int = {
    //  Integer
    x * x * x
  }

  def addTwoEvaluateToABoolean(x: Int)(f: Int => Boolean) = {
    f(x + 2)
  }

  def poly[A, B](a: A, b: B): B = {
    b
  }

  val polyB = poly(true, "")

  val aPadric: String = "padric" + "gill"

  def interpolationExample(something:String) = s"hello $something"

  println(aPadric) // first print line

  val ifGreaterThan5ReturnTrue: Int => Boolean = { (x: Int) =>
    if (x > 5) {
      true
    } else {
      false
    }
  }

  val aBoolean = true

  val aHigherOrderFunctionExample = addTwoEvaluateToABoolean(3)(ifGreaterThan5ReturnTrue)

  println(aHigherOrderFunctionExample) // second println
  println(polyB)
  println(interpolationExample("darkness my old friend"))
}
