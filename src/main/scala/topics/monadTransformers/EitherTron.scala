package topics.monadTransformers

import cats.data.EitherT

import scala.util.Try
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EitherTron {

  def parseDouble(s: String): Either[String, Double] = {
    Try(s.toDouble).map(Right(_)).getOrElse(Left(s"$s is not a number"))
  }

  def divide(a: Double, b: Double): Either[String, Double] = {
    Either.cond(test = b != 0, right = a / b, left = "Cannot divide by zero")
  }

  def divisionProgram(inputA: String, inputB: String): Either[String, Double] = {
    for {
      a <- parseDouble(inputA)
      b <- parseDouble(inputB)
      result <- divide(a, b)
    } yield result
  }

  // Now time to use some futures

  def parseDoubleAsync(s: String): Future[Either[String, Double]] = {
    Future.successful(parseDouble(s))
  }

  def divideAsync(a: Double, b: Double): Future[Either[String, Double]] = {
    Future.successful(divide(a, b))
  }


  // the native solution to compose these functions is long and unreadable especially when you have more future eithers
  def divisionProgramAsyncNative(inputA: String, inputB: String): Future[Either[String, Double]] = {
    parseDoubleAsync(inputA).flatMap { eitherA =>
      parseDoubleAsync(inputB).flatMap { eitherB =>
        (eitherA, eitherB) match {
          case (Right(a), Right(b)) => divideAsync(a, b)
          case (Left(err), _) => Future.successful(Left(err))
          case (_, Left(err)) => Future.successful(Left(err))
        }
      }
    }
  }

  // We can try to remove some boilerplate with EitherT which is a wrapper that does the unpacking, composition and repackaging in the background
  // You can wrap the for loop and in parentheses () and call .value on teh whole thing to get native type

  def divisionProgramAsyncCats(inputA: String, inputB: String): EitherT[Future, String, Double] = {
    for {
      a <- EitherT(parseDoubleAsync(inputA))
      b <- EitherT(parseDoubleAsync(inputB))
      result <- EitherT(divideAsync(a, b))
    } yield result
  }

  val number: EitherT[Option, String, Int] = EitherT.rightT[Option, String](5) // when using rightT or leftT need to use the type annotation
  val error: EitherT[Option, String, Int] = EitherT.leftT[Option, Int]("Not a number")

  // When specifying the container then you can use just .right or .left on EitherT

  val numberO = Some(5)
  val errorStringO = Some("Not a number")
  val numberE: EitherT[Option, String, Int] = EitherT.right(numberO) //again give the type annotation might be an import issue tho not sure
  val errorE: EitherT[Option, String, Int] = EitherT.left(errorStringO)
}

class StarScream {

  def parseDouble(s: String): Either[String, Double] = {
    Try(s.toDouble).map(Right(_)).getOrElse(Left(s"$s is not a n}umber"))
  }

  def divide(a: Double, b: Double): Either[String, Double] = {
    Either.cond(test = b != 0, right = a / b, left = "Cannot divide by zero")
  }

  def divisionProgram(inputA: String, inputB: String): Either[String, Double] = {
    for {
      a <- parseDouble(inputA)
      b <- parseDouble(inputB)
      result <- divide(a, b)
    } yield result
  }

}
