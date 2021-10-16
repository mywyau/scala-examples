package catsStuff.liefBattermann

import cats.data.{NonEmptyList, Validated}
import UserValidationLogic.userValidator

object ValidatedImplementFUserValidator extends App {
                                    //          Validated[ Defined , B]
//  type MyValidated[A] = Validated[NonEmptyList[UserValidationError], A]   // not anonymous but partially applied

    val userValidatorValidatedInterpreter =
      userValidator[
        ({type T[A] = Validated[NonEmptyList[UserValidationError], A]})#T, // We can inline the right projection type alias,so the same as
        NonEmptyList[UserValidationError]                                  // 'MyValidated' lambda partially applied type but with out the name of 'MyValidated'
      ](NonEmptyList(_, Nil))

//  val userValidatorValidatedInterpreterKindProjector =
//    userValidator[MyValidated, NonEmptyList[UserValidationError]](NonEmptyList(_, Nil))

  implicit val userValidatorValidatedInterpreterImpl = userValidatorValidatedInterpreter

  println(UserValidator.validate("John", 25, "john@example.com"))

  println(UserValidator.validate("John", 25, "johnn@example"))
  // Failure(java.lang.Throwable: EmailNotValid)

  println(UserValidator.validate("John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: AgeOutOfRange)

  println(UserValidator.validate(".John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: NameNotValid)

}
