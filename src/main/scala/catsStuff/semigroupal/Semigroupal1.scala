package catsStuff.semigroupal

import cats.Applicative.catsApplicativeForArrow
import cats.Semigroup
import cats.syntax.either._ // for catchOnly

class Semigroupal1 {

  /*  In previous chapters we saw how functors and monads let us sequence opera-
    tions using map and flatMap. While functors and monads are both immensely
    useful abstractions, there are certain types of program flow that they cannot
      represent.
        One such example is form validation. When we validate a form we want to
    return all the errors to the user, not stop on the first error we encounter. If we
      model this with a monad like Either, we fail fast and lose errors. For example,
    the code below fails on the first call to parseInt and doesn’t go any further:*/

  def parseInt(str: String) =
    Either.catchOnly[NumberFormatException](str.toInt)
      .leftMap(_ => s"Couldn't read $str")

  val parseABC =
    for {
      a <- parseInt("a")
      b <- parseInt("b")
      c <- parseInt("c")
    } yield (a + b + c)

  /*
      Another example is the concurrent evaluation of Futures. If we have several
      long-running independent tasks, it makes sense to execute them concurrently.

      However, monadic comprehension only allows us to run them in sequence.
      map and flatMap aren’t quite capable of capturing what we want because
      they make the assumption that each computation is dependent on the previous
      one:

      The calls to parseInt and Future.apply above are independent of one another,
      but map and flatMap can’t exploit this. We need a weaker construct—
      one that doesn’t guarantee sequencing—to achieve the result we want. In this
      chapter we will look at two type classes that support this pa􀂂ern:
  */

  /*

    • Semigroupal encompasses the notion of composing pairs of contexts.
      Cats provides a cats.syntax.apply module that makes use of Semigroupal
      and Functor to allow users to sequence functions with mul-
      tiple arguments.

    • Applicative extends Semigroupal and Functor. It provides a way
      of applying functions to parameters within a context. Applicative is
      the source of the pure method we introduced in Chapter 4.
  */

  /*  Semigroupal
    cats.Semigroupal is a type class that allows us to combine contexts¹. If
    we have two objects of type F[A] and F[B], a Semigroupal[F] allows us to
    combine them to form an F[(A, B)]. Its definition in Cats is:

    trait Semigroupal[F[_]] {
      def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
    }

    As we discussed at the beginning of this chapter, the parameters fa and fb
    are independent of one another: we can compute them in either order before
    passing them to product. This is in contrast to flatMap, which imposes
    a strict order on its parameters. This gives us more freedom when defining
    instances of Semigroupal than we get when defining Monads.

    */

  import cats.Semigroupal
  import cats.instances.option._
  import cats.instances.either._

  val optionProduct: Option[(Int, String)] = Semigroupal[Option].product(Some(123), Some("abc"))

  val noneProduct1 = Semigroupal[Option].product(Some("abc"), None)
  val noneProduct2 = Semigroupal[Option].product(None, Some("abc"))

  val tuple3: Option[(Int, Int, Int)] = Semigroupal.tuple3(Option(1), Option(2), Option(3))
  val tuple3None: Option[(Int, Int, Nothing)] = Semigroupal.tuple3(Option(1), Option(2), None)

  val map3Things = Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _)

  val map4Things = Semigroupal.map4(Option(1), Option(1), Option(1), Option(1))(_ + _ + _ + _)


}
