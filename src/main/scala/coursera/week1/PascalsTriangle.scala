package coursera.week1

import scala.annotation.tailrec

class PascalsTriangle {

  // this implementation is not tail recursive, grows fast
  // note n is also present in the recursive call and it builds up before unwinding when it hits base case. Hence not tail-recursive
  // (4 * (3 * notTailRecFactorial(2))   see the stack grows

  def notTailRecFactorial(n: Int): Int = {
    n match {
      case 0 => 1
      case _ => n * notTailRecFactorial(n - 1)
    }
  }

  // tail recursion allows recursion to occur in a constant stack frame to avoid a stack overflow exceptions.
  // if you data does not need to worry about stack over flow exceptions then clarity trumps efficiency everytime or only worry about performance and
  // optimisations later. Correctness > Clarity > Optimisation  imo.  Donald Knuth: "Premature optimisation is the source of all evil."

  def factorial(n: Int): Int = {
    @tailrec
    def loop(n: Int, accumulator: Int): Int = {
      n match {
        case 0 => accumulator
        case _ => loop(n - 1, n * accumulator) //only a single call - no "n * loop(args)"
      }
    }

    loop(n, 1)
  }


  /* Pascal's Triangle
           columns
           1 2 3 4 5              | totals
rows  0    1                        | 1
      1    1 1                      | 2
      2    1 2 1                    | 4
      3    1 3 3 1                  | 8
      4    1 4  6  4 1              | 16
      5    1 5 10 10 5 1            | 32
      6    1 6 15 20 20 15 6 1      | 64


    The numbers at the edge of the triangle are all 1, and each number inside the triangle is the sum of the two numbers above it
    Write a function that computes the elements of Pascal’s triangle by means of a recursive process.
    Do this exercise by implementing the pascal function in Main.scala, which takes a column c and a row r,
    counting from 0 and returns the number at that spot in the triangle.
    For example, pascal(0,2) =1, pascal(1,2) =2 and pascal(1,3) =3.
  */

//   this version will return a stack overflow exception not tail recursive
    def pascal(column: Int, row: Int): Int = {
      (column, row) match {
        case (c, r) if c == 0 | c == r => 1
        case (c, r) => pascal(c - 1, r - 1) + pascal(c, r - 1)
      }
    }

  def pascal2(row: Int, column: Int): Int = { // <--- correct using math!!!!
    (row, column) match {
      case (_, 0) => 1
      case (r, c) =>
        factorial(r) / (factorial(c) * factorial(if (r - c <= 1) 1 else r - c))  //cannot divide by zero
    }
  }
}
