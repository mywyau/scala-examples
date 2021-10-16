package catsStuff.liefBattermann

import cats.implicits._
import UserValidationLogic.userValidator

object EitherImplementFUserValidator extends App {

  type PartiallyAppliedEither[A] = Either[UserValidationError, A] // needs this for ApplicativeError or it complains

  val userValidatorEitherInterpreter =
    userValidator[PartiallyAppliedEither, UserValidationError](identity)

  //  implicit val eitherInterpreter2 =
  //    userValidator[Either[UserValidationError, *], UserValidationError](identity)   // plugin magic

  //  implicit val eitherInterpreter3 =
  //    userValidator[Either[UserValidationError, *], UserValidationError](identity)   // plugin magic

  //    val eitherInterpreter4 =
  //    userValidator[PartiallyAppliedEither2, UserValidationError](identity)   // plugin magic

  implicit val eitherInterpreter = userValidatorEitherInterpreter

  println(UserValidator.validate("John", 25, "john@example.com"))

  println(UserValidator.validate("John", 25, "johnn@example"))
  // Failure(java.lang.Throwable: EmailNotValid)

  println(UserValidator.validate("John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: AgeOutOfRange)

  println(UserValidator.validate(".John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: NameNotValid)
}