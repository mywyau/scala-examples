package topics.futures

import scala.concurrent.Await
import scala.concurrent.duration._

object Runner extends App {

  val futuresExercise = new FuturesExercise

//  futuresExercise.vanillaDonutStock
//  println(s"Stock of vanilla donut = ${futuresExercise.vanillaDonutStock}")

  val chocoDonutDelivery = Await.result(futuresExercise.dodgyDonutStock("Choco Donut"), 30.seconds)
  println(s"Stock of Choco Donuts = $chocoDonutDelivery")


//  val t = List().map(i => println(i))

}
