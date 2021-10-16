
object HOF {

  val five: Int = 5

  def addFiveMethod(i: Int): Int = i + 5

  //    (Int, Int) => Int
  val add: (Int, Int) => Int = (i: Int, j: Int) => i + j

  val minus = (i: Int, j: Int) => i - j

  val multiply = (i: Int, j: Int) => i * j

  def higherOrderFunction(i: Int, j: Int, operation: (Int, Int) => Int) = operation(i, j)

  def old(i:Int, s:String): Boolean = true

  def scenarios(i: Int, bFunction: Boolean => String, bigDecimalF: BigDecimal => String): String =
    i match {
      case 5 => bFunction(false)
      case 10 => bigDecimalF(100.78674)
    }
}


object Runner extends App {

  //is the only thing thats different
//  //value1, value2, operation --- argument list of the method
//  println(HOF.higherOrderFunction(5, 10, HOF.add))
//  println(HOF.higherOrderFunction(5, 10, HOF.minus))
//  println(HOF.higherOrderFunction(5, 10, HOF.multiply))

  val f = (t:Boolean) => "Mikey"
  val g = (b:BigDecimal) => "Tomy"

  val h = (t:Boolean) => if(t == true) "Mikey" else "Amy"
  val i = (b:BigDecimal) => if(b < 100) "Tomy" else "help"

  val x = (t:Boolean) => if(t == true) "Kill Me" else "Something complicated"
  val y = (b:BigDecimal) => if(b < 100) "Man" else "Kid"


  def scenario1(i: Int): String =
    i match {
      case 5 => f(false)
      case 10 => g(100.78674)
    }

  def scenario2(j: Int): String =
    j match {
      case 5 => h(false)
      case 10 => i(100.78674)
    }

  def scenario3(j: Int): String =
    j match {
      case 5 => x(false)
      case 10 => y(100.78674)
    }

  println(HOF.scenarios(5, h, g))
  println(HOF.scenarios(5, f, g))

  println(scenario1(5))
  println(scenario2(5))
  println(scenario3(5))



}


