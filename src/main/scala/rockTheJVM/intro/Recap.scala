package rockTheJVM.intro

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object Recap {

  val aBoolean = false

  val anIfExpression = if (2 > 3) "bigger" else "small"

  val theUnit = println("Hello Scala") // Unit = "void" in other languages

  // OOP

  class Animal {

    // stuff goes here
  }

  class Cat extends Animal

  trait Carnivore {
    def eat(animal: Animal): Unit
  }

  // inheritance model: extend at most 1 class, but inherit from >= 0 traits

  class Crocodile extends Animal with Carnivore {
    override def eat(animal: Animal): Unit = println("Crunch")
  }

  //singleton

  object MySingleton //singleton pattern in one line - only value of it's type

  //companions
  object Carnivore //companion object of the class Carnivore - the companion object and the class needs to be declared in the same file

  //generics
  class MyList[A] //reuse the same code on many types so more generalised/abstract

  //method notation
  val three = 1 + 2 //infix notation

  val anotherThree = 1.+(2) //explicit plus operand method call on the Int type

  //functional programming
  //work with functions the same as values i.e. first class citizen

  val incrementer: Int => Int = x => x + 1
  val incremented = incrementer(45)

  //HOF - higher order functions - map, flatMap, filter, reduce, fold etc.

  val aList = List(1, 2, 3)
  val processedList = aList.map(incrementer)
  val aLongerList = aList.flatMap(x => List(x, x + 1)) //List(1,2, 2,3, 3,4)

  //Option and Try Monad

  val anOption = Option(3) //Some(3) Option prevent null checking i.e something that might not be present
  val doubledOption: Option[Int] = anOption.map(_ * 2) // convenient since we can apply our HOFs

  val anAttempt: Try[Int] = Try(/* something that might throw*/ 42) // Success(42)
  val aModifiedAttempt = anAttempt.map(_ + 10) // if a Success(42) then add 10 else if Failure() throw an acception I think

  val anUnknown: Any = 45


  // Pattern matching
  val patternMatchString: String =
    anUnknown match {
      case 1 => "first"
      case 2 => "second"
      case _ => "unknown"
    }

  val optionDesc: String =
    anOption match {
      case Some(value) => s"a $value"
      case None => "the option is empty"
    }

  // Futures  after 2.13 avoid global

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  val aFuture = Future {
    42
  }

  // partial functions
  aFuture.onComplete {
    case Success(value) => println(s"the async value fo life is $value")
    case Failure(exception) => println(s"Meaning of value failed $exception")
  }

  val anotherFuture = aFuture.map(incrementer)

  //for-comprehensions

  val aCheckerboard = List(1, 2, 3).flatMap(n => List("a", "b", "c").map(c => (n, c)))
  val anotherCheckerBoard =
    for {
      n <- List(1, 2, 3)
      c <- List("a", "b", "c")
    } yield (n, c) //equivalent to aCheckerboard they are the same

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 43
    case 8 => 56
    case 100 => 99
  }

  //More advanced stuff - Higher Kinded Types

  trait HigherKindedType[F[_]]

  trait SeqChecker[F[_]] {
    def isSequential: Boolean
  }

  val listChecker = new SeqChecker[List] {
    override def isSequential: Boolean = true
  }


  def main(args: Array[String]): Unit = {

  }


}
