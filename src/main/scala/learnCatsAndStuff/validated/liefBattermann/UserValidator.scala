package learnCatsAndStuff.validated.liefBattermann

import cats.Id

final case class User(name: String, age: Int, email: String)

trait UserValidator[F[_]] { // user validator typeclass

  def createValidUser(name: String, age: Int, email: String): F[User]
}

object UserValidator {

  def apply[F[_]](implicit ev: UserValidator[F]): UserValidator[F] = ev

  def validate[F[_] : UserValidator, E](name: String, age: Int, email: String): F[User] =
    UserValidator[F].createValidUser(name, age, email)
}