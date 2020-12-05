package AdventOfCode.ExerciseTwo

import scala.util.matching.Regex

case class PasswordModel(min: Int, max: Int, letter: String, password: String)

class AdventOfCodeTwo {

  val passwords: Seq[String] = AdventOfCodeTwoPasswords.passwords

  val numberPattern: String = "^\\d+-\\d+" //only the beginning number-number
  val passwordPattern: String = "[a-z]+$" //only letter based passwords
  val letterKeyRegex: String = " [a-z]: " //only letterKey

  val wholePassword: String = "^\\d+-\\d+ [a-z]: [a-z]*$" //entire password

  def getNumberPart(password: String) = password.replaceAll(passwordPattern, "").replaceAll(letterKeyRegex, "")

  def getLetterKey(password: String) = password.replaceAll(numberPattern, "").replaceAll(passwordPattern, "").trim.replace(":", "")

  def getPasswordPart(password: String) = password.replaceAll(numberPattern, "").replaceAll(letterKeyRegex, "")

  //  def passWordModelCreator(password: String): PasswordModel =  PasswordModel(password)


}

object ExerciseTwo extends App {

  val passwords: Seq[String] = AdventOfCodeTwoPasswords.passwords

  val model = new AdventOfCodeTwo

  val numbers = model.getNumberPart("1-7 q: qqqqxvqrkbqqztlqlzq")
  val password = model.getPasswordPart("1-7 q: qqqqxvqrkbqqztlqlzq")
  val key = model.getLetterKey("1-7 q: qqqqxvqrkbqqztlqlzq")

  println(numbers)
  println(password)
  println(key)
}
