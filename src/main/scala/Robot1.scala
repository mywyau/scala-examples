//import scala.collection.mutable
//import scala.util.Random

import java.time.LocalDate
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random


class Robot1() {

  //  def futurePlease(date: LocalDate): Future[Either[String, String]] = {
  //    if (date.minusMonths(10) != LocalDate.now())
  //      Future(Left("Hello"))
  //    else
  //      Future(Right("Yass Queen"))
  //  }
  //
  //  def fileLookBack[A](desiredFileDate: LocalDate, numberOfMonthsToLookBack: Int)(
  //    f: LocalDate => Future[Either[String, A]]
  //  ): Future[Either[String, A]] = {
  //
  //    def loop(index: Int, limit: Int, currentDate: LocalDate): Future[Either[String, A]] =
  //      f(currentDate).flatMap {
  //        case _ if index >= limit => Future(Left("Help me"))
  //        case Left(_) =>
  //          loop(index + 1, limit, currentDate.minusMonths(1))
  //        case Right(a) => Future(Right(a))
  //      }
  //
  //    loop(0, numberOfMonthsToLookBack, desiredFileDate)
  //  }


  val random = new Random()

  val alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

  def randStr(n: Int) = (1 to n).map(_ => alpha(Random.nextInt(alpha.length))).mkString

  def randomThreeNumbers: String = random.between(100, 1000).toString

  def createNewName: String = randStr(2) + randomThreeNumbers

  val lazyList = LazyList.fill(20000000)(createNewName)

  val cache = LazyList

  var name = {
    lazyList.iterator.next()
  }

  def reset() = name = ???

  //  private var _name = gen
  //
  //  private def gen = (alnum.filter(_.isUpper).take(2) ++ alnum.filter(_.isDigit).take(3)).mkString
  //
  //  def name = _name
  //
  //  def reset(): Unit = _name = gen

}

object RunnerRobot extends App {

  //  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def futurePlease(date: LocalDate): Future[Either[String, String]] = {
    if (true) {
      println("Left")
      println(Thread.currentThread().toString)
      Future.successful(Left("Hello"))
    } else {
      println("Right")
      println(Thread.currentThread().toString)
      Future.successful(Right("Yass Queen"))
    }
  }


  val arraySize: Int = 1000000

  val tooManyArrays: Int = (Runtime.getRuntime().totalMemory() / arraySize).toInt * 100

  def fileLookBack[A](desiredFileDate: LocalDate, numberOfMonthsToLookBack: Int)(f: LocalDate => Future[Either[String, A]]): Future[Either[String, A]] = {

    def loop(index: Int, limit: Int, currentDate: LocalDate)(implicit ec: ExecutionContext): Future[Either[String, A]] =
      f(currentDate).flatMap {
        case _ if index >= limit =>
          println("hit the limit")
          Future.successful(Left("Help me"))
        case Left(_) =>
          println(s"help me looping: $index")
          loop(index + 1, limit, currentDate)
        case Right(a) =>
          println("I made it out")
          Future.successful(Right(a))
      }

    loop(0, numberOfMonthsToLookBack, desiredFileDate)
  }


  val robot = new Robot1()

  println(fileLookBack(LocalDate.now(), tooManyArrays)(futurePlease).isCompleted)

  //  val alreadySet = mutable.HashSet.empty[String]
  //  for (_ <- 0 until 608399) { // as 6 robot names are generated in the tests above!!
  //    val name = new Robot().name
  //    if (alreadySet contains name) {
  //      println(s"$name is repeated")
  //    }
  //    println(name)
  //    alreadySet += name
  //  }


}