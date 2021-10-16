package catsStuff.semigroup.semigroupK

import cats.SemigroupK
import cats.implicits._
import cats.kernel.Semigroup

case class SomeExampleCaseClass(x: String)

object SemigroupKExample extends App {

  //Normal semigroup  alias for combine |+|

  val combineList: List[Int] = List(1, 2, 3) |+| List(4, 5, 6)
  val combineList2: List[Int] = Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6))

  //SemigroupK  -- There’s a similar typeclass called SemigroupK for type constructors F[_]. alias <+>
  // the 'K' stands for 'kind'

  /*
  SemigroupK has a very similar structure to Semigroup, the difference is that SemigroupK operates on type constructors of one argument.
  So, for example, you can find a Semigroup for types which are fully specified like Int or List[Int] or Option[Int],
  you will find SemigroupK for type constructors like List and Option. These types are type constructors in that you can think of them as
  “functions” in the type space. You can think of the List type as a function which takes a concrete type, like Int, and returns a concrete type: List[Int].
  This pattern would also be referred to having kind: * -> *, whereas Int would have kind * and Map would have kind *,* -> *,
  and, in fact, the K in SemigroupK stands for Kind.
  */

  /*
    SemigroupK is a universal semigroup which operates on kinds.

    This type class is useful when its type parameter F[_] has a
    structure that can be combined for any particular type. Thus,
    SemigroupK is like a Semigroup for kinds (i.e. parametrized
    types).

    A SemigroupK[F] can produce a Semigroup[F[A]] for any type A.

    Here's how to distinguish Semigroup and SemigroupK:

     - Semigroup[A] allows two A values to be combined.

     - SemigroupK[F] allows two F[A] values to be combined, for any A.
       The combination operation just depends on the structure of F,
       but not the structure of A.
   */

  val combineKList = List(1, 2, 3) <+> List(4, 5, 6)
  val combineKList2: List[Int] = SemigroupK[List].combineK(List(1, 2, 3), List(4, 5, 6))

  //  val semigroupOpt: Option[Mikey2] = Mikey2("a").some |+| Mikey2("b").some  does not work
  val semigroupKOpt: Option[SomeExampleCaseClass] = SomeExampleCaseClass("a").some <+> SomeExampleCaseClass("b").some //works

  val righty: Either[Int, String] = Option("a").toRight(1)
  val righty2: Either[Int, String] = Option("b").toRight(1)

  println(righty |+| righty2)
  println(righty <+> righty2)

  // There’s also a subtle difference in the behaviors of the two typeclasses. <+> & |+|
  // The Semigroup will combine the inner value of the Option whereas SemigroupK will just pick the first one.

  // So why use semigroupK?  because semigroupK works on the kind F[A] it doesnt care about A but lets you combine based on F[_] not sure on the results tho

  val collection: Collection[List] = new Collection[List] {

    override def wrap[A](a: A): List[A] = List(a)

    override def first[B](b: List[B]): B = b.head

  }

  /* This works for Option[A] from what I can tell

    Intuition: it is best to think of it as operating only at the F[_] level, never looking into the contents.
    SemigroupK has the convention that it should ignore failures and “pick the first winner”. <+> can therefore be used as
    a mechanism for early exit (losing information) and failure-handling via fallbacks:
  */

}


trait Collection[T[_]] {

  def wrap[A](a: A): T[A]

  def first[B](b: T[B]): B

}