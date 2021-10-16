package scalaCheck

import org.scalacheck.Gen.listOf
import org.scalacheck.{Arbitrary, Gen}
import scalaCheck.StringGenerator.{alphaLowerChar, alphaNumChar}

object AdminGenerator {

  def identifier: Gen[String] = (for {
    c <- alphaLowerChar
    cs <- listOf(alphaNumChar)
  } yield (c :: cs).mkString)

  val adminGen: Gen[Admin] =
    for {
      idValue <- Gen.chooseNum(0, 100)
      nameValue <- identifier
    } yield Admin(idValue, nameValue)

}
