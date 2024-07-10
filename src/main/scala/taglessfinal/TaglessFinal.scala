package taglessfinal

import cats.Monad
import cats.effect.{ExitCode, IOApp}


/* Why tagless final??

  Tagless final (or finally tagless) is a style/encoding of a dsl within a host language. 
  The key advantage of the Tagless Final pattern is that it separates the definition of the DSL from its interpretation, allowing for greater flexibility and composability.
  There are loosely 3 main components to using tagless final within your application. 

    1. The algebraic data type (ADT), which defines the operations of the DSL.
        + The ADT is defined as a set of functions that represent the operations of the DSL. 
        + Each function takes a polymorphic type parameter, which allows the function to operate on any type that satisfies the required constraints.
    2. The Interpreter, which provides a concrete implementation of the DSL.
        + The interpreter is responsible for providing a concrete implementation of the DSL by providing a set of functions that interpret the ADT.
        + The interpreter can be implemented in a variety of ways, such as using a monad, a free monad, or an extensible effects system.

  What is the advantage of this pattern? 😮

  The key advantage of the Tagless Final pattern is that it separates the definition of the DSL from its interpretation, allowing for greater flexibility and composability.

  Cats Effect is a library in Scala that provides a set of abstractions for writing asynchronous, non-blocking code. 
  It is built on top of the Tagless Final pattern, which provides a powerful and flexible way to define and interpret effectful computations.
  In Cats Effect, the Tagless Final pattern is used to represent effectful computations as a set of composable functions, rather than using concrete data types.

  The first time we need to approach a non-trivial program, we must understand how to manage constraints among modules to avoid a functional big ball of mud.
 */

/* 
  For example, let’s build a program that deals with shopping carts on an e-commerce site. First thing first, we need a representation of our domain model:
*/   

// case class Product(id: String, description: String)

// case class ShoppingCart(id: String, products: List[Product])

/* 
 However, it’s not enough. We need some functions to deal with the domain model:
*/  

// def create(id: String): Unit

// def find(id: String): Option[ShoppingCart]

// def add(sc: ShoppingCart, product: Product): ShoppingCart

/* 
  The first problem with these functions is that they perform some side effects for sure. The create function will probably persist information on a database, as will the add function.
  The find function will eventually retrieve a shopping cart from a database, and this operation can eventually fail.
  Fortunately, many Scala libraries (such as Cats Effects, Monix, or even ZIO) allow developers to encapsulate the operation description that produces 
  side effects inside some declarative contexts. Such contexts are called effects. We can use the type of a function to describe the value it will produce
  and the side effects through the effect pattern. Hence, the description of the side effect is separated from its execution. 
*/

// For example, we can use the generic IO[T] effect from Cats. Hence, our function becomes:

// def create(id: String): IO[Unit]

// def find(id: String): IO[Option[ShoppingCart]]

// def add(sc: ShoppingCart, product: Product): IO[ShoppingCart]

/* 
  The best property about using an effect library is to continue to reason about our programs as pure functions. 
  However, we now have to deal with the effect during the testing phase, which isn’t always obvious.
  The second problem with our functions on shopping carts is that we don’t know where to put the code that implements them.
  Should we use a single concrete class? Or should we use an abstract trait and separate implementations?
  In Scala, many patterns have been applied through the years to answer the above-mentioned problems. 
  The Free pattern is one of these. However, many developers have been choosing another pattern in the last few years: The Tagless Final pattern.
  the pattern’s main aim is to use interfaces as much as possible. In the pattern jargon, we call such interfaces algebras.
  Algebras represent the DSL specific to the domain that we want to model. 
  They represent both syntax and semantics of the DSL through the types and the signatures of their functions.
 */

/* And when we need to use this pattern? 💪
    The pattern is particularly useful for use cases involving functional programming and effects in Scala. 
    Some common use cases for this pattern include:

    + Programs involving side effects, such as I/O or database calls, that require high modularity and code reuse.
    + Programs that require high testability, especially those involving side effects, where it is important to write automated tests to ensure that the program functions correctly.
    + Programs that need to be highly scalable and involve the combination and composition of multiple effects.
    + Programs that need to be highly maintainable and flexible over time.
    + In general, the Tagless Final pattern is useful in any situation where greater flexibility, testability, composability, and maintainability are required in functional programming in Scala.
  */



object TaglessFinal extends IOApp {

  import cats.effect.Sync

  // 1. The algebraic data type

  trait ExampleDSL[F[_]] {

    def readString: F[String]

    def writeString(str: String): F[Unit]
  }

  // First Example
  // object ExampleDSL {

  //   def apply[F[_] : Sync]: ExampleDSL[F] =
  //     new ExampleDSL[F] {
  //       override def readString: F[String] = Sync[F].delay(scala.io.StdIn.readLine())

  //       override def writeString(str: String): F[Unit] = Sync[F].delay(println(str))
  //     }
  // }

  // Second Example
  object ExampleDSL {

    def apply[F[_] : Monad]: ExampleDSL[F] =
      new ExampleDSL[F] {                                     // unit type Input/output method
        override def readString: F[String] = Monad[F].pure(scala.io.StdIn.readLine())   // lifting into a Monad

        override def writeString(str: String): F[Unit] = Monad[F].pure(println(str))   // lifting console output
      }
  }

  // 2. The Interpreter

  import cats.effect.IO

  object ExampleInterpreterIO extends ExampleDSL[IO] {

    override def readString: IO[String] = IO(scala.io.StdIn.readLine())

    override def writeString(str: String): IO[Unit] = IO(println(str))
  }

  object ExampleInterpreterOption extends ExampleDSL[Option] {

    override def readString: Option[String] = Option(scala.io.StdIn.readLine())

    override def writeString(str: String): Option[Unit] = Option(println(str))
  }


  // Main
  def run(args: List[String]): IO[ExitCode] = {

    val program2: Option[Unit] =
      for {
        _ <- ExampleDSL[Option].writeString("The Option Interpreter contains an IO effect so running first - please Enter a string:")
        strOpt <- ExampleDSL[Option].readString
        _ <- ExampleDSL[Option].writeString(s"For the Option Interpreter - You entered: $strOpt")
      } yield ()

    val program =
      for {
        _ <- ExampleDSL[IO].writeString("\nIO Interpreter - version Enter a string:") // we bind the effect type at the very end before running our program
//        _ <- ExampleInterpreterIO.writeString("\nIO Interpreter - version Enter a string:") // what we can do instead is use the IO interpreter here since all function calls in the for comprehension are IOs
//        _ <- ExampleInterpreterOption.writeString("\nIO Interpreter - version Enter a string:") // This is not allowed here since it is an Option
        str <- ExampleDSL[IO].readString
        _ <- ExampleDSL[IO].writeString(s"IO Interpreter - version You entered: $str")
      } yield ()

    program.as(ExitCode.Success)
  }
}
