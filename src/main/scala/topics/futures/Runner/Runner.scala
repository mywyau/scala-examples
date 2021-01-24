package topics.futures.Runner

import topics.futures.FuturesExercise

object Runner extends App {

  val futuresExercise = new FuturesExercise

  futuresExercise.vanillaDonutStock
  println(s"Stock of vanilla donut = ${futuresExercise.vanillaDonutStock}")

  val t = List().map(i => println(i))

}
