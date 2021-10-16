package catsStuff.monads

/*Monads are one of the most common abstractions in Scala. Many Scala programmers
quickly become intuitively familiar with monads, even if we don’t
know them by name.
Informally, a monad is anything with a constructor and a flatMap method. All
of the functors we saw in the last chapter are also monads, including Option,
List, and Future. We even have special syntax to support monads: for comprehensions.
However, despite the ubiquity of the concept, the Scala standard
library lacks a concrete type to encompass “things that can be flatMapped”.
This type class is one of the benefits brought to us by Cats.

A monad is a mechanism for sequencing computations.

*/

/* Definition of a Monad

In programming monadic behaviour is formally captured by two behaviours

   • pure, of type A => [A]
   • flatMap, of type (F[A], A => F[B]) => F[B]

pure abstracts  over constructors, providing a way to create a new monadic context from a plain value,
flatMap provides the sequencing step we have already discussed, extracting
the value fom a context and generating the next same context in the sequence.
 */

/*Monad Laws
pure and flatMap must obey a set of laws that allow us to sequence
operations freely without unintended glitches and side-effects:
Le􀁛 iden􀢼ty: calling pure and transforming the result with func is the
same as calling func:
pure(a).flatMap(func) == func(a)
Right iden􀢼ty: passing pure to flatMap is the same as doing nothing:
m.flatMap(pure) == m
Associa􀢼vity: flatMapping over two functions f and g is the same as
flatMapping over f and then flatMapping over g:
m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))*/


import scala.language.higherKinds

trait MikeyMonad[F[_]] {

  def pure[A](a: A): F[A]

  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

  def map[A, B](value: F[A])(func: A => B): F[B] = {
    val f: A => F[B] =
      (a: A) => pure(a = func(a))
    // pure takes func :: A -> B, when func is given an value:A type it converts it into a value:B type. pure then put that B
    // into a F[_] container so pure(func(a)) produces a F[B] which is the desired return type   notice how val f type is like a kleisli arrow A => F[B]
    //This is because monads can compose kleisli arrows
    flatMap(value)((a: A) => pure(func(a)))
  }
}

class Monad1 {

  import cats.Monad
  import cats.instances.list._
  import cats.instances.option._ // for Monad

  val opt1: Option[Int] = Monad[Option].pure(3)
  val opt2: Option[Int] = Monad[Option].flatMap(opt1)(a => Some(a + 2))
  val opt3: Option[Int] = Monad[Option].map(opt2)(b => 100 * b)

  /*  Cats provides instances for all the monads in the standard library (Option,
      List, Vector and so on) via cats.instances:
  */

  val list1: List[Int] = Monad[List].flatMap(List(1, 2, 3))(a => List(a, a * 10))
  //  List[Int] = List(1, 10, 2, 20, 3, 30)

  import cats.instances.vector._ // for Monad

  val vector1 = Monad[Vector].flatMap(Vector(1, 2, 3))(a => Vector(a, a * 10))
  // Vector[Int] = Vector(1, 10, 2, 20, 3, 30)

  // Aswell as Futures, Cats provides a host of new monads that we don’t have in the standard library

  /*
    The syntax for monads comes from three places:
      • cats.syntax.flatMap provides syntax for flatMap;
      • cats.syntax.functor provides syntax for map;
      • cats.syntax.applicative provides syntax for pure.
  */

  /*
    import cats.instances.option._ // for Monad
    import cats.instances.list._ // for Monad
    import cats.syntax.applicative._ // for pure
  */

  import cats.syntax.applicative._ // for pure

  val wrapToOption = 1.pure[Option]

  val wrapToList = 1.pure[List]

  import cats.Monad
  import cats.syntax.flatMap._
  import cats.syntax.functor._


  def sumSquare[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
    a.flatMap(x => b.map(y => x * x + y * y))

  val sumOptions = sumSquare(Option(3), Option(4))
  // res9: Option[Int] = Some(9 + 16) == Some(25)

  val sumLists = sumSquare(List(1, 2, 3), List(4, 5))
  // res9: List[Int] = List(17, 26, 20, 29, 25, 34)


  /*  We can rewrite this code using for comprehensions. The compiler will “do the
  right thing” by rewriting our comprehension in terms of flatMap and map and
    inserting the correct implicit conversions to use our Monad:*/

  def sumSquare2[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] = // for loop implementation
    for {
      x <- a
      y <- b
    } yield x * x + y * y


  // The Identity Monad

  //  .sumSquare method works well on Options and Lists but we can’t call it passing in plain values:
  // sumSquare(3, 4) would blow up since they are not wrapped in a monad/ plain Ints are not monadic

  /*  It would be incredibly useful if we could use sumSquare with parameters that
      were either in a monad or not in a monad at all. This would allow us to abstract
    over monadic and non-monadic code. Fortunately, Cats provides the Id type
    to bridge the gap:*/

  /*  import cats.Id

    sumSquare(3 : Id[Int], 4 : Id[Int])   <--- Id would bridge this gap and make it work for non monadic values
    */

  // a look at Id

  import cats.Id

  val idHandledSumSquare = sumSquare(3: Id[Int], 4: Id[Int])

  val stringId: Id[String] = "Dave": Id[String]
  val intId: Id[Int] = 123: Id[Int]
  val listOfIntsId: Id[List[Int]] = List(1, 2, 3): Id[List[Int]]

  /*
    Cats provides instances of various type classes for Id, including Functor and
    Monad. These let us call map, flatMap, and pure passing in plain values:
  */

  val a = Monad[Id].pure(3)
  val b = Monad[Id].flatMap(a)(_ + 1)

  val addTwoMonads: Id[Int] =
    for {
      x <- a
      y <- b
    } yield x + y

  //The ability to abstract over monadic and non-monadic code is extremely powerful.


}
