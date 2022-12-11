package rockTheJVM.abstractMath

import java.util.concurrent.Executors
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

object MonadTransformers {

  // Convenience API

  //helps you apply map and flatMap without unwrapping and re wrapping up etc.
  def sumAllOptions(values: List[Option[Int]]): Int = ???

  import cats.data.OptionT
  import cats.instances.list._ //fetch an implicit OptionT[List]  - higher kinded type

  val listOfNumberOptions: OptionT[List, Int] = OptionT(List(Option(1), Option(2))) //list of options of Ints  middle, left right is how you read it
  val listOfCharOptions: OptionT[List, String] = OptionT(List(Option("c"), Option("d"))) //list of options of Char  middle, left right is how you read it

  //OptionT lets you manipulate the nested Options in the List without all the clunkiness

  val listOfTuplesFromOptions =
    for {
      n <- listOfNumberOptions
      c <- listOfCharOptions
    } yield (n, c)

  // EitherT

  import cats.data.EitherT

  val listOfNumberEither: EitherT[List, String, Int] = EitherT(List(Left("something wrong"), Right(2), Right(43)))
  val listOfCharEither: EitherT[List, String, String] = EitherT(List(Left("help me"), Right("d"), Right("s")))

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
//  val futureOfEither: EitherT[Future, String, Int] = EitherT(Future[Either[String, Int]](Right(45)))  // need to prove the type to help the compiler out since we are working on a Right

  val futureOfEither: EitherT[Future, String, Int] = EitherT.right(Future(45))  // a convenience method .left and .right on EitherT to help with the boilerplate
  // Future(Right(45)) and wrap this to an EitherT[Future, String, Int]

  val listOfTuplesFromEither: EitherT[List, String, (Int, String)] =
    for {
      n <- listOfNumberEither
      c <- listOfCharEither
    } yield (n, c)


  val bandWidths =
    Map(
      "server1.rockthejvm.com" -> 50,
      "server2.rockthejvm.com" -> 300,
      "server3.rockthejvm.com" -> 170
    )

  type AsyncResponse[T] = EitherT[Future, String, T]

  def getbandWidth(server: String): AsyncResponse[Int] =
    bandWidths.get(server) match {
      case None => EitherT(Future[Either[String, Int]](Left(s"Server $server unreachable")))
      case Some(b) => EitherT(Future[Either[String, Int]](Right(b)))
    }

  //TODO 1:

  def canWithstandSurge(s1: String, s2: String): AsyncResponse[Boolean] =
    for {
      b1 <- getbandWidth(s1)
      b2 <- getbandWidth(s2)
      totalBandWith = b1 + b2
    } yield totalBandWith > 250 // actually dont need to use an if else here neat huh.

  //Future[Either[String, Boolean]]

  //TODO 2:

  def generateTrafficSpikeReport(s1: String, s2: String): AsyncResponse[String] =
    canWithstandSurge(s1, s2).transform {
      case Left(reason) => Left("Servers s1 and s2 cannot cope with the incoming spike")
      case Right(false) => Left("Servers s1 and s2 cannot cope with the incoming spike: not enough total bandwidth")
      case Right(true) => Right("Servers s1 and s2 are able to cope with the incoming spike: NO PROBLEM!!")
    }

  //Future[Either[String, Boolean]]  ----- transformed to desirable type String on the rhs of the Either, Future[Either[String, String]]

  def main(args: Array[String]): Unit = {

    println(listOfTuplesFromOptions.value)
    println(listOfTuplesFromEither.value)

    val copeBandwidth = generateTrafficSpikeReport("server1.rockthejvm.com", "server2.rockthejvm.com").value  //Right
    val unableToCope = generateTrafficSpikeReport("server1.rockthejvm.com", "server3.rockthejvm.com").value  //Left

    println(Await.result(copeBandwidth, 5.seconds)) //Right
    println(Await.result(unableToCope, 5.seconds)) //Left

  }
}
