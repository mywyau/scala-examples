import scala.util.Try

sealed trait CustomErrors

case object NotANumberError extends CustomErrors

case object CannotDivideByZeroError extends CustomErrors

object Ramesh extends App {

  def parseDouble(s: String): Either[CustomErrors, Double] =
    Try(s.toDouble).map(Right(_)).getOrElse(Left(NotANumberError))

  def divide(a: Double, b: Double): Either[CustomErrors, Double] =
    Either.cond(b != 0, a / b, CannotDivideByZeroError)


  def validate(x: String, y: String): Either[CustomErrors, Double] = {
    for {
      x <- parseDouble(x)
      y <- parseDouble(y)
      result <- divide(x, y)
    } yield
      result
  }


  println(validate("5.6", "2.0"))
  println(validate("abc", "2.0"))


}
