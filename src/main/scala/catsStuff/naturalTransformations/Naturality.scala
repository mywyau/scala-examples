package catsStuff.naturalTransformations

import cats.~>

class Naturality {

  val vectorToList = new (Vector ~> List) {
    override def apply[A](fa: Vector[A]): List[A] = fa.toList
  }

  val optToList = new (Option ~> List) {
    override def apply[A](fa: Option[A]): List[A] = fa.toList
  }

  val a: List[Int] = optToList(Some(3))
  assert(List(3) == a)

  val b: List[Boolean] = optToList(Some(true))
  assert(List(true) == b)


}
