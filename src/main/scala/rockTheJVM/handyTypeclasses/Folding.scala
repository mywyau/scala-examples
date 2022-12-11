package rockTheJVM.handyTypeclasses

import cats.{Eval, Monoid}

object Folding {

  //Higher Kinded TC

  //TODO : Implement methods based on foldLeft & foldRight

  import cats.instances.int._

  object ListExercises {

    def map[A, B](la: List[A])(f: A => B): List[B] =
      la.foldRight(List.empty[B])((a, currentList) => f(a) :: currentList)

    def flatMap[A, B](la: List[A])(f: A => List[B]): List[B] =
      la.foldLeft(List.empty[B])((currentList, a) => currentList.foldRight(f(a))(_ :: _))

    def filter[A](la: List[A])(predicate: A => Boolean): List[A] =
      la.foldRight(List.empty[A])((a, currentList) => if (predicate(a)) a :: currentList else currentList)

    def combineAll[A](la: List[A])(implicit monoid: Monoid[A]): A =
      la.foldLeft(monoid.empty)(monoid.combine)

  }

  import cats.Foldable

  import cats.instances.list._ // implicit Foldable[List]  //Foldable is a higher kinded type

  val sumListL = Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _) // 6

  val sumListR = Foldable[List].foldRight(List(1, 2, 3), Eval.now(0))((num, eval) => eval.map(_ + num)) // Eval[Int]  stack safe with fold right using Eval but normally not think
  // Foldleft is usually the one stack safe

  import cats.instances.option._ // implicit Foldable[Option]

  val sumOption = Foldable[Option].foldLeft(Option(2), 30)(_ + _) // 32 //Foldables are useful for generalising on collapse-able structures

  // Extra functionality

  val anotherSum = Foldable[List].combineAll(List(1, 2, 3))
  val mappedConcat = Foldable[List].foldMap(List(1, 2, 3))(_.toString) // usually you need a implicit Monoid[String] we must have it somewhere or already defaulted

  // Nested structures

  val intsNested = List(Vector(1, 2, 3), Vector(4, 5, 6))

  import cats.instances.vector._

  (Foldable[List] compose Foldable[Vector]).combineAll(intsNested) //very convenient nested structures

  // extension methods

  import cats.syntax.foldable._

  val sum3 = List(1, 2, 3).combineAll // req Foldable[List], Monoid[Int]  need the presence of both implicits
  val mappedConcat2 = List(1, 2, 3).foldMap(_.toString)

  def main(args: Array[String]): Unit = {

    import ListExercises._

    val numbers = (1 to 10).toList

    println(map((1 to 10).toList)(_ + 1))
    println(flatMap((1 to 10).toList)(x => (1 to x).toList))

    println(combineAll(numbers))
  }
}
