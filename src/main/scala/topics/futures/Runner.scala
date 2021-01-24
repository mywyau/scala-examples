package topics.futures

object Runner extends App {

  val futuresExercise = new FuturesExercise

  futuresExercise.vanillaDonutStock
  println(s"Stock of vanilla donut = ${futuresExercise.vanillaDonutStock}")

  val t = List().map(i => println(i))

}
