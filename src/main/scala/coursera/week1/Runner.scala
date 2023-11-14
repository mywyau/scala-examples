package coursera.week1

object Runner extends App {

  import scala.annotation.tailrec

  @tailrec
  def factorial(n: Int, acc: Int): Int = {
    n match {
      case 1 => acc
      case number =>
        factorial(number - 1, number * number)
    }
  }

  def factorial2(n: Int): Int = {
    n match {
      case 1 => n
      case number => factorial2(number - 1) * number
    }
  }

  @tailrec
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  println(factorial(5, 1))

  println(factorial2(5))

}
