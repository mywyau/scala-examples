package rockTheJVM.abstractMath

import cats.Monad

import scala.annotation.tailrec

object CustomMonads {

  // pure, flatMap and stack safe iteration functions

  implicit object OptionMonad extends Monad[Option] {

    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa.flatMap(f)

    // we need tailRecM, keeps running if it falls into the A value in the Either
    @tailrec // needs to be tail recursive
    override def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] =
      f(a) match {
        case None => None
        case Some(Left(v)) => tailRecM(v)(f)
        case Some(Right(b)) => Some(b)
      }

    override def pure[A](x: A): Option[A] = Option(x)
  }

  type Identity[T] = T

  val aNumber: Identity[Int] = 42

  implicit object IdentityMonad extends Monad[Identity] {

    override def flatMap[A, B](fa: Identity[A])(f: A => Identity[B]): Identity[B] = f(fa)

    @tailrec // needs to be tail recursive
    override def tailRecM[A, B](a: A)(f: A => Identity[Either[A, B]]): Identity[B] =
      f(a) match {
        case Left(v) => tailRecM(v)(f)
        case Right(b) => b
      }

    override def pure[A](x: A): Identity[A] = x
  }

  sealed trait Tree[+A]

  final case class Leaf[+A](value: A) extends Tree[A]

  final case class Branch[+A](left: Tree[A], right: Tree[A]) extends Tree[A]


  implicit object TreeMonad extends Monad[Tree] {

    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] =
      fa match {
        case Leaf(v) => f(v)
        case Branch(l, r) => Branch(flatMap(l)(f), flatMap(r)(f))
      }

    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = {

      def stackRec(t: Tree[Either[A, B]]): Tree[B] = {
        t match {
          case Leaf(Left(v)) => stackRec(f(v))
          case Leaf(Right(b)) => Leaf(b)
          case Branch(left, right) => Branch(stackRec(left), stackRec(right))
        }
      }

      stackRec(f(a))
    }

    override def pure[A](x: A): Tree[A] = Leaf(x)
  }

  def main(args: Array[String]): Unit = {

    val tree: Tree[Int] = Branch(Leaf(10), Leaf(20))
    val changedTree: Tree[Int] = TreeMonad.flatMap(tree)(v => Branch(Leaf(v + 1), Leaf(v + 2)))
    println(changedTree)

  }

}
