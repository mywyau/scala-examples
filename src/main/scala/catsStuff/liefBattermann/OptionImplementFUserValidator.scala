package catsStuff.liefBattermann

import cats.implicits._

object OptionImplementFUserValidator extends App {

  //  One piece of Scala syntactic noise that often trips people up is the use of type projections to implement anonymous, partially-applied types. For example:
  //
  //  // partially-applied type named "IntOrA"
  //  type IntOrA[A] = Either[Int, A]
  //
  //  // type projection implementing the same type anonymously (without a name).
  //  ({type L[A] = Either[Int, A]})#L

  // https://github.com/typelevel/kind-projector

  import UserValidationLogic.userValidator

  val userValidatorOptionInterpreter =
    userValidator[Option, Unit](_ => ())

  implicit val optionInterpreter = userValidatorOptionInterpreter

  println(UserValidator.validate("John", 25, "john@example.com"))

  println(UserValidator.validate("John", 25, "johnn@example"))
  // Failure(java.lang.Throwable: EmailNotValid)

  println(UserValidator.validate("John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: AgeOutOfRange)

  println(UserValidator.validate(".John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: NameNotValid)
}
