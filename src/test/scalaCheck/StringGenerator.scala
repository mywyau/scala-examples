package scalaCheck

import org.scalacheck.Gen
import org.scalacheck.Gen._

object StringGenerator {

  /** Generates a lower-case alpha character */
  def alphaLowerChar: Gen[Char] = choose(97.toChar, 122.toChar)

  /** Generates an alpha character */
  def alphaChar = frequency((1, alphaUpperChar), (9, alphaLowerChar))

  /** Generates an alphanumerical character */
  def alphaNumChar = frequency((1, numChar), (9, alphaChar))

  //// String Generators ////

  /** Generates a string that starts with a lower-case alpha character,
   * and only contains alphanumerical characters */
  def identifier: Gen[String] = (for {
    c <- alphaLowerChar
    cs <- listOf(alphaNumChar)
  } yield (c :: cs).mkString)


}
