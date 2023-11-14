package catsnotes.monads

import cats._
import cats.implicits._

import scala.annotation.tailrec

case class CustomMonad[A](value: A) {

}


object CustomMonadImp extends App {

  implicit val customMonad = new Monad[CustomMonad] {

    override def pure[A](x: A): CustomMonad[A] = CustomMonad(x)

    override def flatMap[A, B](fa: CustomMonad[A])(f: A => CustomMonad[B]): CustomMonad[B] = {
      f.apply(fa.value)
    }

    @tailrec
    override def tailRecM[A, B](a: A)(f: A => CustomMonad[Either[A, B]]): CustomMonad[B] =
      f(a) match {
        case CustomMonad(either) =>
          either match {
            case Left(a) => tailRecM(a)(f)
            case Right(b) => CustomMonad(b)
          }
      }
  }

  // we will now use our customMonad in a for comprehension

  val monadSequencing: CustomMonad[Int] = {
    for {
      a <- CustomMonad(1)
      b <- CustomMonad(2)
    } yield a + b
  }

  // another exercise
  val addOneF: Int => CustomMonad[Int] = i => CustomMonad(i + 1)
  val mikeyStringF: Int => CustomMonad[String] = i => CustomMonad("Mikey")

  val fishyInt = {
    //    Monad[CustomMonad].pure(1) >>= addOneF
    CustomMonad(1) >>= addOneF
  }

  val customMFishCustomMString = {
    //    Monad[CustomMonad].pure(1) >>= addOneF
    CustomMonad(1) >>= mikeyStringF
  }

  println(monadSequencing)
  println(fishyInt)
  println(customMFishCustomMString)

}
