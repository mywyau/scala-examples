package rockTheJVM.alienConcepts

object Kleislis {

  //generic data structure that helps you in making functions that return a wrapper of A => F[B]

  //Normally we can do this with function composition
  val plainFunc1: Int => String = x => if (x % 2 == 0) s"$x is even" else "failure"
  val plainFunc2: Int => Int = x => x * 3

  val plainFunc3: Int => String = plainFunc2 andThen plainFunc1

  //with wrappers like A => Option[B] we are a little stuck and function composition using andThen or compose does not work, this is where Kleisli comes into practice - Swiss mathematician Heinrich Kleisli

  import cats.data.Kleisli

  val func1: Int => Option[String] = x => if (x % 2 == 0) Some(s"$x is even") else None
  val func2: Int => Option[Int] = x => Some(x * 3)

  //  val func3 = func2 andThen func1  // --- booo stinky does not compile

  //  import cats.instances.option._

  val func1K: Kleisli[Option, Int, String] = Kleisli(func1) // "n is even"
  val func2K: Kleisli[Option, Int, Int] = Kleisli(func2) // multiply by 3

  val func3K: Kleisli[Option, Int, String] = func2K andThen func1K

  // convenience

  val multiply = func2K.map(_ * 2) // x => Option(...).map(_ * 2)
  val chain = func2K.flatMap(x => func1K) // funky

  // Kleisli has a bunch of nice extension methods
  //e.g. func2K.run

  import cats.Id

  type InterestingKleisli[A, B] = Kleisli[Id, A, B] // wrapper[A => Id[B]] // same as a Reader, Reader is implemented as ReaderT but ReaderT is implemented as a Kleisli

  //hint
  val times2 = Kleisli[Id, Int, Int](x => x * 2)
  val plus4 = Kleisli[Id, Int, Int](x => x + 4)

  val composed: Kleisli[Id, Int, Int] =
    times2.flatMap(
      t2 => plus4.map(p4 => t2 + p4)
    )

  val composedFor: Kleisli[Id, Int, Int] =  // Similar to dependency injection i.e. you run it with something so Kleisli is pretty much a Reader when given an Id
    for {
      t2 <- times2
      p4 <- plus4
    } yield t2 + p4  // Monadic but so is Reader...


  def main(args: Array[String]): Unit = {

    println(composedFor(3)) // 13

    println(func3K(6)) //Some(18 is even)

  }
}
