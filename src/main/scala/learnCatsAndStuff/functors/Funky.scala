package scala.learnCatsAndStuff.functors

class Funky {

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

  import cats.instances.function._  // for Functor
  import cats.syntax.functor._      // for map    infix notation thing

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
