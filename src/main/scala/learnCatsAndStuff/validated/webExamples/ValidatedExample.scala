package learnCatsAndStuff.validated.webExamples

import cats._
import cats.data.{NonEmptyList, Validated}
import learnCatsAndStuff.validated.webExamples.UserValidator.userValidatorIdInterpreter

// import cats._
// import cats.data._

import cats.implicits._
// import cats.implicits._

final case class User(name: String, age: Int, email: String)

sealed trait UserValidationError

// defined trait UserValidationError

case object NameNotValid extends UserValidationError

// defined object NameNotValid

case object AgeOutOfRange extends UserValidationError

// defined object AgeOutOfRange

case object EmailNotValid extends UserValidationError

// defined object EmailNotValid


trait UserValidator[F[_]] { // user validator typeclass

  def createValidUser(name: String, age: Int, email: String): F[User]

}

object UserValidator {

  def apply[F[_]](implicit ev: UserValidator[F]): UserValidator[F] = ev

  def validate[F[_] : UserValidator, E](name: String, age: Int, email: String): F[User] =
    UserValidator[F].createValidUser(name, age, email)

  val userValidatorIdInterpreter = new UserValidator[Id] { // typeclass instance

    override def createValidUser(name: String, age: Int, email: String): Id[User] =
      User(name, age, email)
  }
}

object ValidationObjOne {

  implicit val userValidatorInterpreterImp = userValidatorIdInterpreter

  println(UserValidator.validate("John", 25, "john@example.com"))
  // User(John,25,john@example.com)

  println(UserValidator.validate("John", 25, "johnn@example"))
  // User(John,25,johnn@example)

  println(UserValidator.validate("John", -1, "john@gexample"))
  // User(John,-1,john@gexample)

  println(UserValidator.validate(".John", -1, "john@gexample"))
  // User(.John,-1,john@gexample)

}

object ApplicativeFunctorExample {

  def add(a: Int, b: Int) = a + b

  val etaExpandedAdd: (Int, Int) => Int = add _
  //creating a function out of a method, via eta expansion. you can partiall apply and still perform eta expansion to produce a partially applied function from a method

  val curriedAdd: Int => Int => Int = etaExpandedAdd.curried // still pretty much the same function as the two above it just curried.

  (add _).curried.pure[Option] <*> Option(2) <*> Option(5) //remember pure lifts the function into the desired context. In this case Option
  // .curried converts the (Int, Int) to a (Int) => (Int) which is then used to transform add function from '(Int, Int) => Int' to 'Int => Int => Int'

  // <*>  - this is applicative functor, syntax sometimes aka 'tie-fighter' from Star Wars
}

object ValidationObjTwo {

  implicit val userValidatorInterpreterImp = userValidatorIdInterpreter

  def userValidator[F[_], E](mkError: UserValidationError => E)(
    implicit A: ApplicativeError[F, E]): UserValidator[F] =
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

object ImplementFUserValidatorTry extends App {

  import learnCatsAndStuff.validated.webExamples.ValidationObjTwo.userValidator
  import scala.util.Try

  val userValidatorTryInterpreter =
    userValidator[Try, Throwable]((err: UserValidationError) => new Throwable(err.toString))

  type PartiallyAppliedEither[A] = Either[UserValidationError, A] // needs this for ApplicativeError or it complains

  val userValidatorEitherInterpreter =
    userValidator[PartiallyAppliedEither, UserValidationError](identity)

  implicit val tryInterpreter = userValidatorTryInterpreter

  println(UserValidator.validate("John", 25, "john@example.com"))

  println(UserValidator.validate("John", 25, "johnn@example"))
  // Failure(java.lang.Throwable: EmailNotValid)

  println(UserValidator.validate("John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: AgeOutOfRange)

  println(UserValidator.validate(".John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: NameNotValid)
}

object ImplementFUserValidatorOption extends App {

  import learnCatsAndStuff.validated.webExamples.ValidationObjTwo.userValidator

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

  //  One piece of Scala syntactic noise that often trips people up is the use of type projections to implement anonymous, partially-applied types. For example:
  //
  //  // partially-applied type named "IntOrA"
  //  type IntOrA[A] = Either[Int, A]
  //
  //  // type projection implementing the same type anonymously (without a name).
  //  ({type L[A] = Either[Int, A]})#L

  type MyValidated[A] = Validated[NonEmptyList[UserValidationError], A]

  //  val h = attemptDivideApplicativeError[({type T[A] = Validated[String, A]})#T](30, 10)

  val userValidatorValidatedInterpreter =
    userValidator[
      ({type T[A] = Validated[NonEmptyList[UserValidationError], A]})#T, //We can inline the right projection type alias, MyValidated lambda partially applied type
      NonEmptyList[UserValidationError]
    ](NonEmptyList(_, Nil))

  val userValidatorValidatedInterpreterKindProjector =
    userValidator[
      MyValidated,
      NonEmptyList[UserValidationError]
    ](NonEmptyList(_, Nil))
  //userValidatorValidatedInterpreter: UserValidator[[β$0$]cats.data.Validated[cats.data.NonEmptyList[UserValidationError],β$0$]] = $anon$1@36f4fb60

}

object ImplementFUserValidatorEither extends App {

  import learnCatsAndStuff.validated.webExamples.ValidationObjTwo.userValidator

  type PartiallyAppliedEither[A] = Either[UserValidationError, A] // needs this for ApplicativeError or it complains

  val userValidatorEitherInterpreter =
    userValidator[PartiallyAppliedEither, UserValidationError](identity)

  implicit val eitherInterpreter = userValidatorEitherInterpreter

  println(UserValidator.validate("John", 25, "john@example.com"))

  println(UserValidator.validate("John", 25, "johnn@example"))
  // Failure(java.lang.Throwable: EmailNotValid)

  println(UserValidator.validate("John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: AgeOutOfRange)

  println(UserValidator.validate(".John", -1, "john@gexample"))
  // Failure(java.lang.Throwable: NameNotValid)
}