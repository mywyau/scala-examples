package topics.recursion

import scala.annotation.tailrec


class RecursionMania {

  @tailrec
  final def factorial(n: Int, acc: Int): Int = { //    5 * 4 * 3 * 2 * 1 = 120

    n match {
      case 0 => acc // 1
      case _ => factorial(n - 1, acc * n)
    }
  }

  def factorial2(number: Int): Int = {

    @tailrec
    def loop(n: Int, acc: Int): Int = {

      n match {
        case 0 => acc
        case myNum if myNum > 0 => loop(n - 1, acc * n)
        case _ =>
          println(s"[RecursionMania][factorial2] you tried to perform factorial on a negative number ($n) please get it together :)")
          n
      }
    }

    loop(number, 1)
  }

  @tailrec
  final def sumAll(xs: Seq[Int], accStart: Int): Int = {

    xs match {
      case Nil => accStart
      case h :: tail => sumAll(tail, accStart + h)
    }
  }

  final def sumAll2(ns: Seq[Int]): Int = {

    @tailrec
    def loop(xs: Seq[Int], acc: Int): Int = {
      xs match {
        case Nil => acc
        case h :: tail => loop(tail, acc + h)
      }
    }

    loop(ns, 0)
  }

}


class Bird(name: String) {

  val birdName: String = name

  def fly() = s"I am a $name bird and I am flying high in the sky"

}

object RecursionManiaRunner extends App {

  val recursionManiaObj = new RecursionMania()
  val numbers: Seq[Int] = Seq(1, 2, 3, 4, 5)

  val robin = new Bird(name = "Robin")
  val crow = new Bird(name = "Crow")

  //  val factorial = (i: Int) => recursionManiaObj.factorial(n = i, acc = 1)
  //  val factorialEta: (Int, Int) => Int = recursionManiaObj.factorial _
  //  val factorialCooked: Int => Int = factorialEta.curried(_)(1)
  //
  //  //  val sumAllNums = (js: Seq[Int]) => recursionManiaObj.sumAll(js, 0)
  //
  //  val sumAllNums2 = (js: Seq[Int]) => recursionManiaObj.sumAll2(js)

  //  println(recursionManiaObj.factorial2(5)) // 120 correct
  //  println(recursionManiaObj.factorial2(10)) // 362880 correct
  //  println(recursionManiaObj.factorial2(-5)) // hits the third case which throws an Exception and blows up the program
  //  //  println(factorialCooked(5))
  //  //
  //  //  println(sumAllNums(numbers))
  //  //  println(numbers.foldLeft(0)(_ + _))
  //
  //  //  println(sumAllNums(numbersX))
  //  //  println(recursionManiaObj.factorial(n = 5, acc = 1))
  //
  //  //  println(sumAllNums2(numbers))

  println(robin.birdName)
  //  println(robin.fly())
  //  println(crow.fly())
}


