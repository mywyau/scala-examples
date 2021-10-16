package scalaCheck

import org.scalacheck.Gen.choose
import org.scalacheck.{Gen, Properties}

object StringSpecification extends Properties("String") {

  import org.scalacheck.Prop.forAll

  //https://www.scalacheck.org/

  // strGen generates a fixed length random string
  val strGen = (n: Int) => Gen.listOfN(n, Gen.alphaChar).map(_.mkString)

  val fixedLengthStr = forAll(strGen(10)) { s =>
    s.length == 10
  }

  fixedLengthStr.check

  def alphaLowerChar: Gen[Char] = choose(97.toChar, 122.toChar)


  val classToTest = new StringMethods

  val methodToTest: (String, String) => String = classToTest.appCombinedStringMethod _

  val myGenerator = StringGenerator.identifier

  property("startsWith") = forAll(myGenerator, myGenerator) { (a: String, b: String) =>
    methodToTest(a, b).startsWith(a)
  }

  property("concatenate") = forAll { (a: String, b: String) =>
    methodToTest(a, b) == (a + b)
  }

  property("get second part of string via substring") = forAll { (a: String, b: String) =>
    methodToTest(a, b).substring(a.length, a.length + b.length) == b
  }

  property("get first part of string via substring") = forAll(myGenerator, myGenerator) { (a: String, b: String) =>

    val actual = methodToTest(a, b).substring(0, a.length)
    println("actual = " + actual)

    val expected = a
    println("expected = " + expected)

    println(s"substring a = $a,    substring b = $b,   full string = ${methodToTest(a, b)}\n")

    methodToTest(a, b).substring(0, a.length) == a
  }

}