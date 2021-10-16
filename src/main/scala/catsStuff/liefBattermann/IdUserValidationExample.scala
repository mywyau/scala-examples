package catsStuff.liefBattermann

import cats.Id

object IdUserValidationExample extends App {

  val userValidatorIdInterpreter = new UserValidator[Id] { // typeclass instance using Id as the F[_] so no change

    override def createValidUser(name: String, age: Int, email: String): Id[User] =
      User(name, age, email)
  }

  implicit val userValidatorInterpreterImp = userValidatorIdInterpreter
  // this uses Id context as a morphism to produce and preserve a User type

  println(UserValidator.validate("John", 25, "john@example.com"))
  // User(John,25,john@example.com)

  println(UserValidator.validate("John", 25, "johnn@example"))
  // User(John,25,johnn@example)

  println(UserValidator.validate("John", -1, "john@gexample"))
  // User(John,-1,john@gexample)

  println(UserValidator.validate(".John", -1, "john@gexample"))
  // User(.John,-1,john@gexample)

}
