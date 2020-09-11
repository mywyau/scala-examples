package Futures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

class Concurrency {

  def donutStock(donut: String): Future[Int] = Future {
    // Assume a super long database running operation
    Thread.sleep(10000)
    println("checking donut stock")
    10
  }

  val vanillaDonutStock: Int = Await.result(donutStock("vanilla donut"), 5.seconds)
  println(s"Stock of vanilla donut = $vanillaDonutStock")

  val complete = donutStock("vanilla donut").onComplete {
    case Success(stock) => println(s"Stock for vanilla donut = $stock")
    case Failure(e) => println(s"Failed to find vanilla donut stock, exception = $e")
  }

  def buyDonuts(quantity: Int): Future[Boolean] = Future {
    println(s"buying $quantity donuts")
    true
  }

  val buyingDonuts: Future[Boolean] = donutStock("plain donut").flatMap(qty => buyDonuts(qty))

  println("\nStep 3: Chaining Futures using for comprehension")

  val loopyFutures: Future[Unit] = for {
    stock <- donutStock("vanilla donut")
    isSuccess <- buyDonuts(stock)
  } yield println(s"Buying vanilla donut was successful = $isSuccess")

  val loop: Future[Boolean] = for {
    stock <- donutStock("choco donut")
    s <- buyDonuts(stock)
  } yield s   //same as flatmap chaining futures

}
