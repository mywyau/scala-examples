package topics.klesliArrows

import KleisliPartTwo._

object Runner extends App {

  val upperCaseThenToWords = process("Hello there my name is mikey and I am attempting to learn Kleisli categories")
  val mikey = mikeyWackyStringFactory("Kleisli categories")

  println(upperCaseThenToWords)
  println(mikey)


}
