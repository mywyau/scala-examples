package learnCatsAndStuff.monads

import cats.Id
import cats.data.WriterT

/*
  The Writer Monad

  cats.data.Writer is a monad that lets us carry a log along with a computation.
  We can use it to record messages, errors, or additional data about a
  computation, and extract the log alongside the final result.

  One common use for Writers is recording sequences of steps in multi-threaded
  computations where standard imperative logging techniques can result
  in interleaved messages from different contexts. With Writer the log for
  the computation is tied to the result, so we can run concurrent computations
  without mixing logs.

 in short Writers are useful for logging operations in multi-threaded environments.

*/

class WriterMonad {

  import cats.data.Writer
  import cats.instances.vector._ // for Monoid

  val logSomething: WriterT[Id, Vector[String], Int] = Writer(Vector("It was the best of times", "it was the worst of times"), 1859)

  /*  Notice that the type reported on the console is actually WriterT[Id, Vector[
      String], Int] instead of Writer[Vector[String], Int] as we
      might expect. In the spirit of code reuse, Cats implements Writer in terms
    of another type, WriterT. WriterT is an example of a new concept called a
    monad transformer, which we will cover in the next chapter.
    Let’s try to ignore this detail for now. Writer is a type alias for WriterT, so
    we can read types like WriterT[Id, W, A] as Writer[W, A]:*/

  // type Writer[W, A] = WriterT[Id, W, A]

  type Logged[A] = Writer[Vector[String], A]

  import cats.syntax.applicative._ //for .pure from Applicative

  val logNumber: Logged[Int] = 123.pure[Logged]

  import cats.syntax.writer._ // for .tell

  // If we have a log and no result we can create a Writer[Unit] using the tell

  //  this returns a unit () as the second parameter in the Writer, the first being the log. So the result = nothing
  //  our log does not necessarily need to be a Vector but want an efficient append and concatenation operation so why not Vector

  val createWriterNoResult: Writer[Vector[String], Unit] = Vector("msg1", "msg2", "msg3", "this writer uses .tell - result = Unit").tell
  // res3: cats.data.Writer[scala.collection.immutable.Vector[String], Unit] = WriterT((Vector(msg1, msg2, msg3),()))

  // If we have both a result and a log, we can either use Writer.apply or we can use the writer syntax from cats.syntax.writer:

  val writerA: WriterT[Id, Vector[String], Int] = Writer(Vector("msg1", "msg2", "msg3", "writerA created normally"), 123) // two different ways of creating a Writer with our desired Vector[String]
  val writerB: Writer[Vector[String], Int] = 123.writer(Vector("msg1", "msg2", "msg3", "writerB created via .writer method"))

  // we can extract the result and log using .value

  val aResult: Id[Int] = writerA.value
  val aLog: Id[Vector[String]] = writerA.written

  // we can extract both values at the same time using the run method

  val (bLog, bResult) = writerB.run
  // log: scala.collection.immutable.Vector[String] = Vector(msg1, msg2, msg3)
  // result: Int = 123

  // Composing and transforming Writers

  /*
    The log in a Writer is preserved when we map or flatMap over it. flatMap
    appends the logs from the source Writer and the result of the user’s sequencing
    function. For this reason it’s good practice to use a log type that has an
    efficient append and concatenate operations, such as a Vector:
  */

  // writer monad composition
  val writer1: WriterT[Id, Vector[String], Int] =
    for {
      a <- 10.pure[Logged]
      _ <- Vector("a", "b", "c").tell
      b <- 32.writer(Vector("x", "y", "z"))
    } yield a + b

  val writer1b: WriterT[Id, Vector[String], Int] = writer1.map(result => result + 9000)

  // In addition to transforming the result with map and flatMap, we can transform the log in Writer with mapWritten method:
  val writer2a: WriterT[Id, Vector[String], Int] = writer1.mapWritten(_.map(_.toUpperCase))

  val writer3a: WriterT[Id, Vector[String], Int] =
    writer1.bimap(
      log => log.map(_.reverse),    // bimap is a bifunctor which takes two functions and acts out the results of each function to their respective components.
      result => result * 100        // i.e. a => a'  and b => b' at the same time so it works on a category E which is a product of categories C x D.
    )                               // the function on a does not interact with b and b's functions only acts on the b component. log => log, result => result
                                    // product is a bifunctor, applies functions in parallel to respective components of the product

  // for sum types for example Either[A, B], Either is also a bifunctor. each component A or B has it's own function which evaluates to it's own result
  // Right(b) => a + 2,     Left(a) => a + 5  i.e. 2 separate functions for projections Right or Left

  val writer3b: WriterT[Id, Vector[String], Int] =
    writer1.mapBoth { (log, result) =>
      val log2 = log.map(_ + "!!")
      val result2 = result * 1000
      (log2, result2)
    }

  //  Finally, we can clear the log with the reset method and swap log and result with the swap method:

  val resetWriter3b = writer3b.reset

  val swapWriter3b = writer3b.swap
}

object WriterRunner extends App {

  val writerMonad = new WriterMonad

  println(writerMonad.createWriterNoResult)

  println(writerMonad.writerA)
  println(writerMonad.writerB)

  println(writerMonad.aResult + " - .value pulls out the result of writerA \n")
  println(writerMonad.aLog + " - .written pulls out the log of writerA \n")

  println(writerMonad.bResult + "\n") // both log and result from writerB pulled out in single val via .run
  println(writerMonad.bLog + "\n")

  println(writerMonad.writer1.run) // Writer composition using for loop answer is 42 since it yields a + b = 10 + 32

  println("\n" + writerMonad.writer1b.run + " - used map") // when writer is 'mapped over' we can manipulate the result but not the log

  println("\n" + writerMonad.writer2a.run + " - used mapWritten") // mapWritten is what we need to manipulate the log

  println("\n" + writerMonad.writer3a.run + " - used biMap") // bimap / mapBoth is what we need to manipulate the log and the result at the same time

  println("\n" + writerMonad.writer3b.run + " - used mapBoth") // bimap / mapBoth is what we need to manipulate the log and the result at the same time

  println("\n" + writerMonad.resetWriter3b.run + " - used .reset") // reset - we can erase the Vector log with .reset

  println("\n" + writerMonad.swapWriter3b.run + " - used .swap") // swap - we can switcheroo the log and result via .swap

}