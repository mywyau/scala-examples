package KlesliArrows

class Kleisli {

  val twice: Int => Int = x => x * 2

  val countCats: Int => String = x => if (x == 1) "1 cat" else s"$x cats"

  val twiceAsManyCats: Int => String =
    twice andThen countCats // equivalent to: countCats compose twice

  twiceAsManyCats(1) // "2 cats"

  //Sometimes, our functions will need to return monadic values. For instance, consider the following set of functions.

  val parse: String => Option[Int] = s => if (s.matches("-?[0-9]+")) Some(s.toInt) else None
  val reciprocal: Int => Option[Double] = i => if (i != 0) Some(1.0 / i) else None

  /*
   At its core, Kleisli[F[_], A, B] is just a wrapper around the function A => F[B]. Depending on the properties of the F[_],
   we can do different things with Kleislis. For instance, if F[_] has a FlatMap[F] instance (we can call flatMap on F[A] values),
   we can compose two Kleislis much like we can two functions
  */

  import cats.FlatMap
  import cats.implicits._

  final case class Kleisli[F[_], A, B](run: A => F[B]) {
    def compose[Z](k: Kleisli[F, Z, A])(implicit F: FlatMap[F]): Kleisli[F, Z, B] =
      Kleisli[F, Z, B](z => k.run(z).flatMap(run))
  }

}
