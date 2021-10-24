package rockTheJVM.dataManipulation

import cats.Semigroup
import rockTheJVM.dataManipulation.DataValidation.FormValidationSolution.validateFormAnswer

import scala.util.Try

object DataValidation {

  //Validated

  import cats.data.Validated

  //acts like an Either

  val aValidValue: Validated[String, Int] = Validated.valid(42) //Right value
  val anInvalidValue: Validated[String, Int] = Validated.invalid("Something wrong")
  val aTest: Validated[String, Int] = Validated.cond(42 > 39, 99, "meaning of life is too small")

  /*
    - n must be a prime
    - n must be non-negative
    - n <= 100
    - n must be even
  */

  private def testPrime(n: Int): Boolean = {
    def tailrecPrime(d: Int): Boolean = {
      if (d <= 1) true
      else n % d != 0 && tailrecPrime(d - 1)
    }

    if (n == 0 || n == 1 || n == -1) false
    else tailrecPrime(Math.abs(n / 2))
  }

  def testNumber(n: Int): Either[List[String], Int] = {
    val isNotEven: List[String] = if (n % 2 == 0) List() else List("Number must be even")
    val isNegative: List[String] = if (n >= 0) List() else List("Number must be non-negative")
    val isTooBig: List[String] = if (n <= 100) List() else List("Number must be less than or equal to 100")
    val isNotPrime: List[String] = if (testPrime(n)) List() else List("Number must be less than or equal to 100")

    if (n % 2 == 0 && n >= n && n <= 100 && testPrime(n)) Right(n)
    else Left(isNotEven ++ isNegative ++ isTooBig ++ isNotPrime)
  }

  import cats.instances.list._

  implicit val combineIntMax: Semigroup[Int] = Semigroup.instance[Int](Math.max)

  def validateNumber(n: Int): Validated[List[String], Int] = {
    Validated.cond(n % 2 == 0, n, List("Number must be even"))
      .combine(Validated.cond(n >= 0, n, List("Number must be non-negative")))
      .combine(Validated.cond(n <= 100, n, List("Number must be less than 100")))
      .combine(Validated.cond(testPrime(n), n, List("Number must be a prime")))
  }

  // chaining validated instances
  aValidValue.andThen(_ => anInvalidValue) //there is no flatMap because if the original value is invalid then there is no more transformation and errors are should not be accumulated
  //flatMap would short circuit the chains

  // test a valid value
  aValidValue.ensure(List("something went wrong"))(_ % 2 == 0)

  aValidValue.map(_ + 1)

  aValidValue.leftMap(_.length) // manipulate the errors
  aValidValue.bimap(_.length, _ + 1) //manipulate both projections

  val eitherToValidated: Validated[List[String], Int] = Validated.fromEither(Right(42))
  val optionToValidated: Validated[List[String], Int] = Validated.fromOption(Some(42), List("Something went wrong")) // Some(42) could be replaced with None
  val tryToValidated: Validated[Throwable, Int] = Validated.fromTry(Try(42)) // with try has to be Throwable on LHS

  // backwards - from Validated to standard library

  aValidValue.toOption
  aValidValue.toEither

  //TODO 2: Form validation exercise

  /*
  *  - name, email and password must be specified
  *  - name must not be blank
  *  - email must be a valid email with @
  *  - password must have 10 characters
  */


  object FormValidation {

    type FormValidation[T] = Validated[List[String], T]

    //    def validateName(n: String) =
    //      if (n != "") Valid("name is okay")
    //      else Invalid(List("name must not be blank"))
    //
    //    def validateEmail(e: String) =
    //      if (!e.contains("@")) Valid("email is valid")
    //      else Invalid(List("email does not contain an @ symbol"))
    //
    //    def validatePassword(p: String) =
    //      if (p.length >= 10) Valid("password is >= 10 chars")
    //      else Invalid(List("password must not be less than 10 characters"))

    //    def validateName(n: String) = n != ""
    //
    //    def validateEmail(e: String) = !e.contains("@")
    //
    //    def validatePassword(p: String) = p.length >= 10
    //
    //    import cats.instances.map._
    //    import cats.instances.list._
    //
    //    def validatedForm(form: Map[String, String]): FormValidation[String] =
    //      Validated.cond(
    //        form.isDefinedAt("name") && form.isDefinedAt("email") && form.isDefinedAt("password"),
    //        "All fields completed",
    //        List("one or all name, email, and password unspecified")
    //      )
    //        .combine(Validated.cond(validateName(form.getOrElse("name", "")), form, List("name is blank")))
    //      .combine(Validated.cond(validateEmail(form.getOrElse("email", "")), form, List("name is blank")))
    //      .combine(Validated.cond(validatePassword(form.getOrElse("password", "")), form, List("name is blank")))
  }

  object FormValidationSolution {

    //    import cats.instances.string._

    type FormValidationSolution[T] = Validated[List[String], T]

    def getValue(form: Map[String, String], fieldName: String): FormValidationSolution[String] =
      Validated.fromOption(form.get(fieldName), List(s"the field $fieldName must be specified"))

    def nonBlank(value: String, fieldName: String): FormValidationSolution[String] =
      Validated.cond(value.length > 0, value, List(s"The field $fieldName must not be blank"))

    def emailProperForm(email: String): FormValidationSolution[String] =
      Validated.cond(email.contains("@"), email, List(s"Email is invalid"))

    def passwordCheck(password: String): FormValidationSolution[String] =
      Validated.cond(password.length >= 10, password, List(s"Password must be at least 10"))

    def validateFormAnswer(form: Map[String, String]) =
      getValue(form, "Name").andThen(name => nonBlank(name, "Blank"))
        .combine(getValue(form, "Email").andThen(emailProperForm))
        .combine(getValue(form, "Password").andThen(passwordCheck))
        .map(_ => "User registration complete.")

  }

  import cats.syntax.validated._

  val aValidMeaningOfLife: Validated[List[String], Int] = 42.valid[List[String]]      // if using the extension methods you should probably define the other projection's type
  val error: Validated[String, Int] = "Something".invalid[Int]


  def main(args: Array[String]): Unit = {

    val successfulForm1 =
      Map(
        "Name" -> "bob jones",
        "Email" -> "bobJones@gmail.com",
        "Password" -> "password1234567890"
      )

    val invalidForm2 =
      Map(
        "Name" -> "bob jones",
        "Email" -> "bobJones@gmail.com",
        "Password" -> "password1"
      )

    val invalidForm3 =
      Map(
        "Name" -> "bob jones",
        "Email" -> "bobJones@gmail.com"
      )

    println(validateFormAnswer(successfulForm1))
    println(validateFormAnswer(invalidForm2))
    println(validateFormAnswer(invalidForm3))

  }
}
