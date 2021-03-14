package learnCatsAndStuff.monads

import cats.Id
import cats.data.WriterT


/*
  The Writer Monad

  cats.data.Writer is a monad that lets us carry a log along with a computa
  􀦞on. We can use it to record messages, errors, or addi􀦞onal data about a
  computa􀦞on, and extract the log alongside the final result.

  One common use for Writers is recording sequences of steps in mul􀦞-
threaded computa􀦞ons where standard impera􀦞ve logging techniques can result
in interleaved messages from different contexts. With Writer the log for
the computa􀦞on is 􀦞ed to the result, so we can run concurrent computa􀦞ons
without mixing logs.
*/

class WriterMonad {

  import cats.data.Writer
  import cats.instances.vector._ // for Monoid

  val logSomething: WriterT[Id, Vector[String], Int] = Writer(Vector("It was the best of times", "it was the worsrt of times"), 1859)

  /*  No􀦞ce that the type reported on the console is actually WriterT[Id, Vector[
      String], Int] instead of Writer[Vector[String], Int] as we
      might expect. In the spirit of code reuse, Cats implements Writer in terms
    of another type, WriterT. WriterT is an example of a new concept called a
    monad transformer, which we will cover in the next chapter.
    Let’s try to ignore this detail for now. Writer is a type alias for WriterT, so
    we can read types like WriterT[Id, W, A] as Writer[W, A]:*/

  // type Writer[W, A] = WriterT[Id, W, A]

}
