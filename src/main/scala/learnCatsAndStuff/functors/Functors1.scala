package scala.learnCatsAndStuff.functors

class Functors1 {

  /*
    We will investigate functors, an abstraction that allows us to
    represent sequences of operations within a context such as a List, an Option,
    or any one of a thousand other possibilities.

    Functors on their own are not so
    useful, but special cases of functors such as monads and applicative functors
    are some of the most commonly used abstractions in Cats.

    Informally, a functor is anything with a map method. You probably know lots
    of types that have this: Option, List, and Either, to name a few.

    We typically first encounter map when iterating over Lists. However, to understand
    functors we need to think of the method in another way. Rather than
    traversing the list, we should think of it as transforming all of the values inside
    in one go. We specify the function to apply, and map ensures it is applied to
    every item. The values change but the structure of the list remains the same:
  */

  val noob = List(1, 2, 3).map(n => n + 1)

  /*
    We should think of map not as an iteration pattern, but as a way of sequencing
    computations on values ignoring some complication dictated by the relevant
    data type:
  */

  /*  Functions (?!)

    It turns out that single argument functions are also functors. To see this we
    have to tweak the types a little. A function A => B has two type parameters:
    the parameter type A and the result type B. To coerce them to the correct
    shape we can fix the parameter type and let the result type vary:

      • start with X => A;
      • supply a function A => B;
      • get back X => B.

    If we alias X => A as MyFunc[A], we see the same pattern of types we saw
    with the other examples in this chapter. We also see this in Figure 3.3:

      • start with MyFunc[A];
      • supply a function A => B;
      • get back MyFunc[B].

    In other words, “mapping” over a Function1 is function composition:
  */

  /*
    Par􀢼al Unification

    For the above examples to work we need to add the following compiler
    option to build.sbt:

      scalacOptions += "-Ypartial-unification"
  */

  import cats.instances.function._
  import cats.syntax.functor._ // for map    infix notation thing & since your not working on Option[A], List[B] etc.

  val f: Int => Double = (x: Int) => x.toDouble // X => A

  val g: Double => String = (y: Double) => y.toString // A => B

  //  composition of functions g after f

  val func3a: Int => String = (f map g) (_) // X => B    composition of two function, the underscore it to keep it partially applied since we're using map function

  val func3b: Int => String = f andThen g // X => B    composition of two function, the underscore it to keep it partially applied since we're using map function

  val func3c: Int => String = (i: Int) => g(f(i)) // X => B    composition of two function, the underscore it to keep it partially applied since we're using map function

  // Renamed Book example

  val h: Int => Double = (x: Int) => x.toDouble // X => A

  val i: Double => Double = (y: Double) => y.toDouble // A => B

  //  composition of functions g after f

  val funky1: Int => Double = (h map i) (_) // X => B   composition of two function, the underscore it to keep it partially applied since we're using map function

  val funky2: Int => Double = h andThen i // X => B    composition of two function using andThen

  val funky3: Int => Double = (j: Int) => i(h(j)) // X => B    composition of two function written out by hand but not fully applied

  val finalForm: Int => String = {
    ((x: Int) => x.toDouble)
      .map(a => a + 1)
      .map(a => a * 2)
      .map(a => a + "!")
  }
}

class Functors2 {

  /*
    Every example we’ve looked at so far is a functor: a class that encapsulates
    sequencing computations. Formally, a functor is a type F[A] with an operation
    map with type (A => B) => F[B].
  */

  import scala.language.higherKinds


  /*  Higher Kinds and Type Constructors - Types of a Higher Kind

    Kinds are like types for types. They describe the number of “holes” in a type.   - a hole is like List[_],   [_] looks kinda like a 'hole' or a pitfall trap/well of water

    We distinguish between regular types that have no holes and “type constructors”
    that have holes we can fill to produce types.

    For example, List is a type constructor with one hole. We fill that hole by
    specifying a parameter to produce a regular type like List[Int] or List[A].

    The trick is not to confuse type constructors with generic types. List is a type
    constructor, List[A] is a type:
      - List // type constructor, takes one parameter
      - List[A] // type, produced using a type parameter - a solid type

    There’s a close analogy here with functions and values. Functions are “value constructors” —
    they produce values when we supply parameters:
      - math.abs // function, takes one parameter  i.e. guess you can call it a 'value constructor'
      - math.abs(x) // value, produced using a value parameter - a solid end value
  */

  import cats.Functor
  import cats.instances.list._
  import cats.instances.option._

  import scala.language.higherKinds // for Functor

  val list1: List[Int] = List(1, 2, 3)

  val list2: List[Int] = Functor[List].map(list1)(_ * 2) // Kinda like Monoid[A] we can define an instance for Functor allowing use the 'map' function

  val option1: Option[Int] = Option(123)

  val option2 = Functor[Option].map(option1)(_.toString)

  /*
    Functor also provides the lift method, which converts a function of
    type A => B to one that operates over a functor and has type F[A] => F[B]:
  */

  val func: Int => Int = (x: Int) => x + 1

  val liftFuncInToOption: Option[Int] => Option[Int] = Functor[Option].lift(func) // liftedFunc: Option[Int] => Option[Int]

  val applyLiftedFunc: Option[Int] = liftFuncInToOption(Option(1))

  /*
    Options and Lists have their own map functions so hard to demonstrate

    Let’s look at mapping over functions. Scala’s Function1 type does not
    have a map method (it’s called andThen instead) so there are no naming conflicts:
  */

  import cats.instances.function._
  import cats.syntax.functor._ // for map

  val func1 = (a: Int) => a + 1
  val func2 = (a: Int) => a * 2
  val func3 = (a: Int) => a + "!"
  val func4 = func1.map(func2).map(func3)

  func4(123) // res1: String = 248!

  /*
    This time abstract over functors so we're not working on any specific example
    We can write a method that applies an equation to a number no matter what functor context it's in
  */


  // we will use parametric polymorphism to define a higher kinded type F[_], so we can use it as a parameter type for our method

  def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] =
    start.map(n => n + 1 * 2)

  val doMathOnOptions: Option[Int] = doMath(Option(20))
  val doMathOnLists: List[Int] = doMath(List(1, 2, 3))

  /*
    To illustrate how this works, let’s take a look at the definition of the map
    method in cats.syntax.functor. Here’s a simplified version of the code:

    |  implicit class FunctorOps[F[_], A](src: F[A]) {
    |    def map[B](func: A => B)(implicit functor: Functor[F]): F[B] =   <----   this is the extension method
    |      functor.map(src)(func)
    |  }

    The compiler can use this extension method to insert a map method wherever
    no built-in map is available:

    | foo.map(value => value + 1)     <---- foo is assumed to not have an implemented .map() method so FunctorOps helps it out

    Assuming foo has no built-in map method, the compiler detects the potential
    error and wraps the expression in a FunctorOps to fix the code:

    |  new FunctorOps(foo).map(value => value + 1)

    The map method of FunctorOps requires an implicit Functor as a parameter.
    This means this code will only compile if we have a Functor for F in scope.

    If we don’t, we get a compiler error:

    |  final case class Box[A](value: A)
    |  val box = Box[Int](123)
    |  box.map(value => value + 1)

      <console>:34: error: value map is not a member of Box[Int]
        - Box[Int] is custom higher kinded type/ type constructor, possibly no Box[Int] implicit
   */

  import scala.concurrent.{Future, ExecutionContext}

  implicit def futureFunctor(implicit ec: ExecutionContext): Functor[Future] = {
    new Functor[Future] {
      override def map[A, B](fa: Future[A])(f: A => B): Future[B] =
        fa.map(f)
    }
  }

}


trait Printable[A] {
  self =>

  def format(value: A): String

  def contramap[B](func: B => A): Printable[B] =
    new Printable[B] {
      override def format(valueB: B): String = self.format(value = func(valueB))
    }
}

final case class Box[A](value: A)

trait PrintableInterface {

  implicit val stringPrintable: Printable[String] =
    new Printable[String] {
      def format(value: String): String =
        "\"" + value + "\""
    }

  implicit val booleanPrintable: Printable[Boolean] =
    new Printable[Boolean] {
      def format(value: Boolean): String =
        if (value) "yes" else "no"
    }

  implicit def boxPrintable[A](implicit printable: Printable[A]): Printable[Box[A]] =
    new Printable[Box[A]] {
      def format(box: Box[A]): String =
        printable.format(box.value)
    }

  implicit def boxPrintable2[A](implicit printable: Printable[A]): Printable[Box[A]] =
    printable.contramap[Box[A]](_.value)

}

class Printable extends  PrintableInterface {

  val formatBoxString =


}





