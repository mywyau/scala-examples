package calculator

object LibbyHelloWorld extends App {

  println("hello world")

  // methods

  def addSeven(x: Int) = {
    x + 7
  }

  //  println(addSeven(5))
  //  println(addSeven(100))

  val aWord: String = "Hello Libby"

  val wordSeven: String = "7"
  val seven: Int = 7 + 5

  //  println(aWord.reverse)

  def booleanLogic(x: Int) = {
    if (x == 10) "the number is 10"
    else s"the number is anything else but 10 it is actually $x"
  }

  //  println(booleanLogic(10))
  //  println(booleanLogic(20))

  val aListOfNumbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

  val aListOfNumbersIncremtedByOne: List[Int] = aListOfNumbers.map(x => x + 1)

  val aListOfListOfNumbers: Seq[Seq[Int]] = Seq(Seq(1, 2, 3), Seq(4, 5, 6), Seq(7, 8, 9))

  val chars: Seq[Seq[String]] = Seq(Seq("a", "b", "c"))

  val aListOfListOfNumbersAddOne: Seq[Int] = Seq(Seq(1, 2, 3), Seq(4, 5, 6), Seq(7, 8, 9)).flatMap(i => i.map(j => j + 1))

  // for comprehension
  val exampleForComprehension =
    for {
      aListOfListOfNumbersAddOne <- Seq(Seq(1, 2, 3), Seq(4, 5, 6), Seq(7, 8, 9))
      c <- chars
    } yield {
      aListOfListOfNumbersAddOne
    }

  aListOfListOfNumbers

  //  println(aListOfNumbers)
  //  println(aListOfNumbersIncremtedByOne)

  println(exampleForComprehension)
}
