package learnCatsAndStuff.functors

import cats.implicits._

class Bifunctors {

  // Bifunctors require two functions, for Either it enables you the option to manipulate both projections

  val right1: Either[String, Int] = Right(100)
  val left1: Either[String, Int] = Left("uwu")

  val right2: Either[String, Int] = right1.bimap(
    _.toUpperCase,
    x => x * 2
  )

  val left2 = left1.bimap(
    _.toUpperCase,
    x => x * 2
  )

  // .leftMap only manipulates the Left projection

  val rightL = right1.leftMap(x => x + "hiya")
  val leftL = left1.leftMap(x => x + "hiya")

  // regular .map only manipulates the Right projection

  // .bifoldMap


}

object BifunctorRunner extends App {

  val bifunctors = new Bifunctors

  println(bifunctors.right1)
  println(bifunctors.left1)

  println(bifunctors.right2)
  println(bifunctors.left2)

  println(bifunctors.rightL)
  println(bifunctors.leftL)

}