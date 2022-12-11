package rockTheJVM.dataManipulation

import cats.Id
import cats.data.Writer

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Writers {

  // data type - logging Writer Monad

  val aWriter: Writer[List[String], Int] = Writer(List("Started something"), 45)
  val aIncreasedWriter: Writer[List[String], Int] = aWriter.map(_ + 1) // value increases but log stays the same
  val aLogsWriter: Writer[List[String], Int] = aWriter.mapWritten(_ :+ "found something intresting") // value stays the same but log changes

  val aWriterWithBoth = aWriter.bimap(_ :+ "found something interesting", _ + 1) // manipulates both value and logs
  val aWriterWithBoth2 = aWriter.mapBoth {
    (logs, value) => (logs :+ s"found something interesting $value", value + 1) // mapBoth differs to bimap since you can mix value in the logs and also the logs in the value
  }

  import cats.instances.vector._

  // can use semigroup to concatenate the Writer logs

  val writerA = Writer(Vector("Log A1", "Log A2"), 10)
  val writerB = Writer(Vector("Log B1"), 40)
  //maybe you perform some manipulations on the value type, you are able to keep a log with your value

  //3 - dump either the value or the logs
  val desiredValue: Id[Int] = aWriter.value
  val logs: Id[List[String]] = aWriter.written
  val (l, v) = aWriter.run

  //reset the logs

  import cats.instances.list._ //a Monoid[List[Int]]

  val emptyWriter = aWriter.reset //resets the logs but keeps the value - only works in the implicit of a Monoid type

  val compositeWriter = for {
    va <- writerA
    vb <- writerB
  } yield va + vb

  // Benefit #1: we work with pure FP

  def countAndSay(n: Int): Unit = {
    if (n <= 0) println("starting!")
    else {
      countAndSay(n - 1)
      println(n)
    }
  }

  def countAndLog(n: Int): Writer[Vector[String], Int] = {
    if (n <= 0) Writer(Vector("starting"), 0)
    else countAndLog(n - 1).flatMap(_ => Writer(Vector(s"$n"), n))
  }

  //TODO 2: Another benefit of Writer

  def naiveSum(n: Int): Int = {
    if (n <= 0) 0
    else {
      println(s"Now at $n")
      val lowerSum = naiveSum(n - 1)
      println(s"Computed sum(${n - 1}) = $lowerSum")
      lowerSum + n
    }
  }

  def sumWithLogs(n: Int): Writer[Vector[String], Int] =
    if (n <= 0) Writer(Vector(), 0)
    else for {
      _ <- Writer(Vector(s"Now at $n"), n)
      lowerSum <- sumWithLogs(n - 1)
      _ <- Writer(Vector(s"Computed sum(${n - 1})"), n)
    } yield lowerSum + n

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  def main(args: Array[String]): Unit = {
    println(compositeWriter.run)
    //ex 1
    countAndLog(10).written.foreach(println)

    //ex 2
    Future(naiveSum(100)).foreach(println)  // we will attempt to use println to log or side effects to log stuff out vs a Writer. They will be interspersed from the futures vs the Writer
    Future(naiveSum(100)).foreach(println)
    sumWithLogs(100).written.foreach(println)

    // now observing the difference under different threads - asynchronous programming
  }

}
