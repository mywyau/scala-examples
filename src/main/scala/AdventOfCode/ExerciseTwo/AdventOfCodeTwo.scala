package AdventOfCode.ExerciseTwo

case class PasswordModel(min: Int, max: Int, letter: String, password: String) {

  def validation: Boolean = {
    val letterKeyCount = password.count(_ == letter.charAt(0))
    if (letterKeyCount >= min && letterKeyCount <= max) {
      true
    } else {
      false
    }
  }

  def partTwoValidation: Boolean = {

    val firstAppearance: Char = password.charAt(min - 1)
    val secondAppearance: Char = password.charAt(max - 1)

    (firstAppearance, secondAppearance) match {
      case (first, second) if first == letter.charAt(0) && second != letter.charAt(0) => true
      case (first, second) if first != letter.charAt(0) && second == letter.charAt(0) => true
      case _ => false
    }
  }

}

class AdventOfCodeTwo {

  val listOfPasswords: Seq[String] = AdventOfCodeTwoPasswords.passwords

  val numberPattern: String = "^\\d+-\\d+" //only the beginning number-number
  val minPattern: String = "^\\d+-" //only the min number and hyphen
  val maxPattern: String = "-\\d+$" //only the min number and hyphen
  val passwordPattern: String = "[a-z]+$" //only letter based passwords
  val letterKeyRegex: String = " [a-z]: " //only letterKey

  val wholePassword: String = "^\\d+-\\d+ [a-z]: [a-z]*$" //entire password

  def getNumberPart(password: String): String = {
    password.replaceAll(passwordPattern, "").replaceAll(letterKeyRegex, "")
  }

  def getMin(numberKey: String): Int = numberKey.replaceAll(maxPattern, "").toInt

  def getMax(numberKey: String): Int = numberKey.replaceAll(minPattern, "").toInt

  def getLetterKey(password: String): String = {
    password.replaceAll(numberPattern, "")
      .replaceAll(passwordPattern, "")
      .trim.replace(":", "")
  }

  def getPasswordPart(password: String): String = password.replaceAll(numberPattern, "").replaceAll(letterKeyRegex, "")

  val minMaxList: Seq[String] = listOfPasswords.map(data => getNumberPart(data))
  val minList: Seq[Int] = minMaxList.map(data => getMin(data))
  val maxList: Seq[Int] = minMaxList.map(data => getMax(data))
  val letterList: Seq[String] = listOfPasswords.map(data => getLetterKey(data))
  val passwordList: Seq[String] = listOfPasswords.map(data => getPasswordPart(data))

  def passwordModelCreator(): Seq[PasswordModel] = {

    val zippedTogether: Seq[(Int, Int, String, String)] = minList zip maxList zip letterList zip passwordList map {
      case (((min, max), key), password) => (min, max, key, password)
    }

    for {
      passwordData <- zippedTogether
    } yield PasswordModel(passwordData._1, passwordData._2, passwordData._3, passwordData._4)
  }

}

object ExerciseTwo extends App {

  val passwords: Seq[String] = AdventOfCodeTwoPasswords.passwords
  val totalNumberOfPasswords: Int = AdventOfCodeTwoPasswords.passwords.size

  val model = new AdventOfCodeTwo

  val numbers = model.getNumberPart("1-7 q: qqqqxvqrkbqqztlqlzq") //test the string strippers to get the desired components: min, max, key, password
  val password = model.getPasswordPart("1-7 q: qqqqxvqrkbqqztlqlzq")
  val letterKey = model.getLetterKey("1-7 q: qqqqxvqrkbqqztlqlzq")

  val minKey = model.getMin(numbers)
  val maxKey = model.getMax(numbers)

  val minList = model.minList
  val maxList = model.maxList
  val letterList = model.letterList
  val passwordList = model.passwordList

  val minListSize = model.minList.size //all lists size == 1000

  val seqOfPasswordModels = model.passwordModelCreator()

  val seqOfValidatedPasswords: Seq[Boolean] = seqOfPasswordModels.map(_.validation)
  val removeAllInvalidPasswords: Seq[Boolean] = seqOfValidatedPasswords.filter(_ == true)
  val sizeOfValidPasswordCollection = removeAllInvalidPasswords.length


  println(s"The total number of passwords = $totalNumberOfPasswords")
  println(s"Test number key with hyphen: $numbers")
  println(s"for password $password the min key: $minKey")
  println(s"for password $password the min key: $maxKey")
  println(s"Test key: $letterKey")
  println(s"Test password: $password")
  println(s"number of min key = $minListSize")
  println(model.passwordList) // seq iof data only password part
  println(seqOfPasswordModels) // password data is now wrapped in Password case classes
  println(seqOfValidatedPasswords) //applied the validation to seq of Password models
  // to tell me if password passes the validation given in requirements, return trues and falses

  println(removeAllInvalidPasswords) //filter and collect only the true
  println(sizeOfValidPasswordCollection) //return the size of the list of valid passwords (true)

  //part two

  val seqOfValidatedPasswords2: Seq[Boolean] = seqOfPasswordModels.map(_.partTwoValidation)
  val removeAllInvalidPasswords2: Seq[Boolean] = seqOfValidatedPasswords2.filter(_ == true)
  val sizeOfValidPasswordCollection2 = removeAllInvalidPasswords2.length

  println(seqOfValidatedPasswords2)
  println(removeAllInvalidPasswords2)
  println(sizeOfValidPasswordCollection2)

  def partTwoValidation2(min: Int, max: Int, letter: String, password: String): Boolean = {

    val firstAppearance: Char = password.charAt(min - 1)
    val secondAppearance: Char = password.charAt(max - 1)

    (firstAppearance, secondAppearance) match {
      case (first, second) if first == letter.charAt(0) && second != letter.charAt(0) =>
        println(password)
        println(firstAppearance)
        println(secondAppearance)
        true
      case (first, second) if first != letter.charAt(0) && second == letter.charAt(0) =>
        println(password)
        println(firstAppearance)
        println(secondAppearance)
        true
      case _ =>
        println(password)
        println(firstAppearance)
        println(secondAppearance)
        false
    }
  }

  println(partTwoValidation2(1,7,"q", "qqqqxvqrkbqqztlqlzq"))
  println(partTwoValidation2(4,5,"t", "wcjtfpt"))

}
