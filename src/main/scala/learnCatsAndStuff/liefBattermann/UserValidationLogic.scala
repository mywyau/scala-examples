package learnCatsAndStuff.liefBattermann

import cats.ApplicativeError
import cats.implicits._

object UserValidationLogic {

  def userValidator[F[_], E](mkError: UserValidationError => E)(implicit A: ApplicativeError[F, E]): UserValidator[F] =
    new UserValidator[F] {

      def validateName(name: String): F[String] =
        if (name.matches("(?i:^[a-z][a-z ,.'-]*$)")) name.pure[F]
        else A.raiseError(mkError(NameNotValid))

      def validateAge(age: Int): F[Int] =
        if (age >= 18 && age < 120) age.pure[F]
        else A.raiseError(mkError(EmailNotValid))

      def validateEmail(email: String): F[String] =
        if (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
          email.pure[F]
        else A.raiseError(mkError(EmailNotValid))

      override def createValidUser(name: String, age: Int, email: String): F[User] =
        (User.apply _).curried.pure[F] <*> validateName(name) <*> validateAge(age) <*> validateEmail(email)
    }

}
