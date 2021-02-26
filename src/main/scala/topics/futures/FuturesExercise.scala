package topics.futures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

class FuturesExercise {

  //  def donutStock(donut: String): Future[Int] = {
  //    Future {
  //      Thread.sleep(3000)
  //      // Assume a super long database running operation in this case 3 seconds changing
  //      // this above 5000ms will kill it because of 'val vanillaDonutStock' only allows at most 5 seconds Await
  //      println(s"checking $donut stock")
  //      10 //return value
  //    }
  //  }
  //
  //  val vanillaDonutStock: Int = {
  //    Await.result(
  //      awaitable = donutStock("vanilla donut"),
  //      atMost = 5.seconds
  //    )
  //  }
  //
  //  val complete: Unit = {
  //    donutStock("vanilla donut").onComplete {
  //      case Success(stock) => println(s"Stock for vanilla donut = $stock")
  //      case Failure(e) => println(s"Failed to find vanilla donut stock, exception = $e")
  //    }
  //  }
  //
  //  def buyDonuts(quantity: Int): Future[Boolean] = {
  //    Future {
  //      println(s"buying $quantity donuts")
  //      true
  //    }
  //  }
  //
  //  val buyingDonuts: Future[Boolean] = {
  //    donutStock("plain donut").flatMap(
  //      quantity => buyDonuts(quantity)
  //    )
  //  }
  //
  //  println("\nStep 3: Chaining Futures using 'for comprehension' instead of flatMaps to look nicer")
  //
  //  val loopyFutures: Future[Unit] = {
  //    for {
  //      stock <- donutStock("vanilla donut")
  //      isSuccess <- buyDonuts(stock)
  //    } yield println(s"Buying vanilla donut was successful = $isSuccess")
  //  }
  //
  //  val loop: Future[Boolean] = {
  //    for {
  //      stock <- donutStock("choco donut")
  //      s <- buyDonuts(stock)
  //    } yield s //same as flatmap chaining futures
  //  }


  def dodgyDonutStock(donut: String): Future[Int] = {
    Future {
      Thread.sleep(5000)
      // Assume a super long database running operation in this case 3 seconds changing
      // an Await with above 20 seconds duration will kill it so keep it like 30s or something
      println(s"checking bad $donut stock")
      Thread.sleep(15000)
      throw new NullPointerException // <-----  yikes we forced it to blow up with a Null Pointer, think of the children :'(
      2000000000 // return value 2 million donuts which we will never get to eat :(
    }.recover {
      case e: NullPointerException =>
        println(s"The donut system crashed and threw a $e but we saved it with a recover that has a partial function")
        1000000000 //  <----- yay saved but we lost half of our donuts :(
    }
  }

}
