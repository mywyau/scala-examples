package scalaCheck

import org.scalacheck.{Arbitrary, Gen, Properties}
import scalaCheck.AdminGenerator.adminGen
import scalaCheck.StringGenerator.identifier

object AccountSpec extends Properties("AccountMethod") {

  import org.scalacheck.Prop.forAll

  val classToTest = new AccountMethods

  val methodToTest: Account => String = classToTest.weeby _

  val genOneOfName: Gen[String] =
    Gen.oneOf("Mikey", "Kouhai", "Senpai")

  val myUserGenerator: Gen[User] =
    for {
      intValue <- Gen.chooseNum(1, 9000)
      genName: String <- genOneOfName
    } yield User(intValue, genName)

  val myUserGeneratorIdOver9000: Gen[User] =
    for {
      intValue <- Gen.chooseNum(9001, 15000)
      genName: String <- identifier
    } yield User(intValue, genName)

  val genKouhaiOrSenpai: Gen[String] = Gen.oneOf("Kouhai", "Senpai")

  val myUserGeneratorIdLessThanEqual9000KouSen: Gen[User] =
    for {
      intValue <- Gen.chooseNum(1, 9000)
      genName: String <- genKouhaiOrSenpai
    } yield User(intValue, genName)


  // User Tests

  property("check response contains users name") =
    forAll(myUserGenerator) { (genUser: User) =>
      //      println(s"${methodToTest(genUser)}  ${genUser.name}")
      methodToTest(genUser).contains(genUser.name)
    }

  property("check id is over 9000!! & return correct response") =
    forAll(myUserGeneratorIdOver9000) { (genUser: User) =>
      //      println(s"${methodToTest(genUser)}  ${genUser.id}")
      methodToTest(genUser).contains("It's over 9000!!")
    }

  property("check user name is valid for when id is <= 9000!!") =
    forAll(myUserGeneratorIdLessThanEqual9000KouSen) { (genUser: User) =>
      //      println(s"${methodToTest(genUser)}  ${genUser.id}  $genUser")
      methodToTest(genUser).contains("Kouhai") || methodToTest(genUser).contains("Senpai")
    }

  property("check admin name is valid for when id is <= 100!!") =
    forAll(myUserGeneratorIdLessThanEqual9000KouSen) { (genUser: User) =>
      //      println(s"${methodToTest(genUser)}  ${genUser.id}  $genUser")
      methodToTest(genUser).contains("Kouhai") || methodToTest(genUser).contains("Senpai")
    }

  // Admin Tests - because of the implicit defined below we do no need to supply a generator for the forAll for Admin

  implicit lazy val arbitraryAdminGen =
    Arbitrary[Admin](adminGen)

  property("check admin name is valid for when id is <= 100!!") =
    forAll { (genAdmin: Admin) =>
      println(s"${methodToTest(genAdmin)}  ${genAdmin.id}  $genAdmin")
      methodToTest(genAdmin).contains("Run it's a manager!!")
    }


}