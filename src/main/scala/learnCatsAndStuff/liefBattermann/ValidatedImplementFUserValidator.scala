package learnCatsAndStuff.liefBattermann

import cats.data.{NonEmptyList, Validated}
import UserValidationLogic.userValidator

object ValidatedImplementFUserValidator extends App {

  type MyValidated[A] = Validated[NonEmptyList[UserValidationError], A]

  //  val userValidatorValidatedInterpreter =
  //    userValidator[
  //      ({type T[A] = Validated[NonEmptyList[UserValidationError], A]})#T, //We can inline the right projection type alias, MyValidated lambda partially applied type
  //      NonEmptyList[UserValidationError]
  //    ](NonEmptyList(_, Nil))

  val userValidatorValidatedInterpreterKindProjector =
    userValidator[MyValidated, NonEmptyList[UserValidationError]](NonEmptyList(_, Nil))

  implicit val userValidatorValidatedInterpreterImpl = userValidatorValidatedInterpreterKindProjector

  println(UserValidator.validate("John", 25, "john@example.com"))

  println(UserValidator.validate("John", 25, "johnn@example"))
  // Failure(java.lang.Throwable: EmailNotValid)

  println(UserValidator.validate("John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: AgeOutOfRange)

  println(UserValidator.validate(".John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: NameNotValid)

}
