package learnCatsAndStuff.validated.liefBattermann

import cats.implicits._

object TryImplementFUserValidator extends App {

  import learnCatsAndStuff.validated.liefBattermann.UserValidationLogic.userValidator
  import scala.util.Try

  val userValidatorTryInterpreter =
    userValidator[Try, Throwable]((err: UserValidationError) => new Throwable(err.toString))

  implicit val tryInterpreter = userValidatorTryInterpreter

  println(UserValidator.validate("John", 25, "john@example.com"))

  println(UserValidator.validate("John", 25, "johnn@example"))
  // Failure(java.lang.Throwable: EmailNotValid)

  println(UserValidator.validate("John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: AgeOutOfRange)

  println(UserValidator.validate(".John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: NameNotValid)
}
