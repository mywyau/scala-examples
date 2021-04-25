package learnCatsAndStuff

import cats._, cats.data._, cats.implicits._

object ArrowExample extends App {

  // >>> is an operator for 'andThen'
  // <<< is an operator for 'compose'

  val f = (_: Int) + 1
  val g = (_: Int) * 100

  val andThenExample = (f >>> g) (2) // first 2 + 1 = 3, andThen * 100 = 300   fo g(f(x))  g after f
  val composeExample = (f <<< g) (2) // first * 100 then + 1  = 201        f(g(x)) f after g

  println(andThenExample)
  println(composeExample)

  val f_first = f.first[Int]     // remember function 'f' just adds one
  val addOneToFirst = f_first((1, 1))
  val f_second = f.second[Int]
  val addOneToSecond = f_second((5,5))

  println(addOneToFirst)
  println(addOneToSecond)

}
