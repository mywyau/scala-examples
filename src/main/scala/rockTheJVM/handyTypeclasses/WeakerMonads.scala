package rockTheJVM.handyTypeclasses

import cats.{Applicative, Apply, FlatMap}

object WeakerMonads {

  trait MyFlatMap[M[_]] extends Apply[M] {

    // suppose FlatMap was to extend Apply can it implement .ap(), well yes it can so it is natural for FlatMap to extend Apply - there is a relationship
    // that is the reasoning kinda FlatMap could easily not extend Apply but since it can easily use it's methods and Apply's functor methods to implement .ap()
    // therefore it makes sens for it to extend Apply

    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]

    //    hint: Apply extends Functor
    //    def ap[A, B](wf: M[A => B])(wa: M[A]): M[B] = {
    //      // My solution
    //
    //      flatMap(wf)((f: A => B) => map(wa)(a => f(a)))
    //    }

    def ap[A, B](wf: M[A => B])(wa: M[A]): M[B] = {
      //       answer

      flatMap(wa)(a => map(wf)(f => f(a)))
      // M[A] A => M[A=>B] A=>B  B    -- which overall gets flatMapped into M[B] for the return type
    }
  }

  import cats.syntax.flatMap._
  import cats.syntax.functor._ // .map extension method  // lets you perform for comprehensions

  // notice it is similar to Monad context bound version but this time with a Weaker Monad version
  def getPairs[M[_] : FlatMap, A, B](ma: M[A], mb: M[B]): M[(A, B)] =
    for {
      n <- ma
      c <- mb
    } yield (n, c)


  def getPairsIntString(ma: Seq[Int], mb: Seq[String]): Seq[(Int, String)] =
    for {
      n <- ma
      c <- mb
    } yield (n, c)

  // essentially we can perform for comprehensions with the least constraint which is FlatMap not Monad
  // Monad does not have it's own new methods it just extends FlatMap
  // We can probably use FlatMap for weirder typeclasses which are crazier than Monad for example


  trait MyMonad[M[_]] extends Applicative[M] with MyFlatMap[M] {

    // Just again for avoiding collision sake MyFlatMap is already in Cats
    // .pure() lives in Applicative and map needs override since Applicative also already defines .map() probably from Functor via Apply

    override def map[A, B](ma: M[A])(f: A => B): M[B] =
      flatMap(ma)(x => pure(f(x)))
  }


  def main(args: Array[String]): Unit = {

    val nums = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    val letters = Seq(
      "a",
      "b",
      "c",
      "d",
      "e",
      "f",
      "g",
      "h",
      "i",
      "j"
    )

    def sum(n: Int): Int = {

      def loop(acc: Int, start: Int): Int = {
        if (start == n) {
          Thread.sleep(500)
          println(s"$acc + $start = ${acc + start}")
          acc + start
        } else {
          Thread.sleep(500)
          println(s"$acc + $start = ${acc + start}")
          loop(acc + start, start + 1)
        }
      }

      loop(0, 1)
    }


    // 1 x 2 x 3 x 4 x 5 = 120  ?

    // 1 + 2 + 3 + 4 + 5 = 15  ?
    // 1 + 2 + 3 + 4 + 5 + ... 100 = 5050x
    // 1 + 2 + 3 + 4 + 5 + ... 100 =

    println(sum(20))

    //    getPairsIntString(nums, letters).foreach(println)

  }
}
