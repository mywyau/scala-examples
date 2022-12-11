package rockTheJVM.abstractMath

import scala.util.Try

object Functors {

  val aModifiedList = List(1, 2, 3).map(_ + 1) //List(2,3,4)
  val aModifiedOption = Option(2).map(_ + 1) //Some(3)  // None
  val aModifiedTry = Try(42).map(_ + 1) //Success(43) // Failure

  trait myFunctor[F[_]] {

    def map[A, B](fa: F[A])(a: A => B): F[B]
  }

  import cats.Functor
  import cats.instances.list._

  val listFunctor = Functor[List]

  val incrementedNumbers = listFunctor.map(List(1, 2, 3))(_ + 1)

  import cats.instances.option._

  val optionFunctor = Functor[Option]
  val incrementedOption = optionFunctor.map(Option(2))(_ + 1)

  import cats.instances.try_._

  val anIncrementedTry = Functor[Try].map(Try(42))(_ + 1)

  // why? standard library alread supports it and this seems more complicated

  // so generalised API

  // we will define some method for some types of F[_]

  def do10xList(list: List[Int]) = list.map(_ * 10)

  def do10xOption(option: Option[Int]): Option[Int] = option.map(_ * 10)

  def do10xTry(myTry: Try[Int]) = myTry.map(_ * 10)

  // our fancy abstract version now

  def do10x[F[_]](container: F[Int])(implicit functor: Functor[F]): F[Int] =
    functor.map(container)(_ * 10)

  import cats.syntax.functor._ // need this for functor helper

  def do10xShorter[F[_]: Functor](container: F[Int]): F[Int] = {
    container.map(_ * 10)
  }

  // Exercise 1 - define own functor

  trait Tree[+T]

  case class Leaf[+T](value: T) extends Tree[T]

  case class Branch[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T]


  // smart constructors - companion object

  // this might be cleaner and safer type wise

  object Tree {

    def leaf[T](value: T): Tree[T] = Leaf(value) // look a the return type it's not the subtype of Leaf[T]  but Tree[T]

    def branch[T](value: T, left: Tree[T], right: Tree[T]): Tree[T] = Branch(value, left, right) // look a the return type it's not the subtype of Leaf[T]  but Tree[T]

    // the return type of the type signature is the way it is because we need Functor[Tree] NOT Functor[Branch] or Functor[Leaf]
  }

  //

  implicit object TreeFunctor extends Functor[Tree] {

    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = {
      fa match {
        case Leaf(v) => Leaf(f(v))
        case Branch(v, l, r) => Branch(f(v), map(l)(f), map(r)(f))
      }
    }
  }

  // extension methods - provides .map() for your own data structure so long you have an implicit Functor value for your data structure in scope

  import cats.syntax.functor._

  def do10xShorterVersion[F[_]](container: F[Int])(implicit functor: Functor[F]): F[Int] =
    container.map(_ * 10)
  // look how this is shorter than the original implementation due to the extension methods provided by Cats

  def do10xShorterVersion2[F[_] : Functor](container: F[Int]): F[Int] =
    container.map(_ * 10)
  // we remove the implicit since it is unused buuut we need to add a type context to F[_] to Functor to communicate to the compiler that we are working with Functor

  val myTree: Tree[Int] =
    Tree.branch(
      40,
      Tree.branch(5, Tree.leaf(10), Tree.leaf(30)),
      Tree.leaf(20)
    )

  val incrementedTree = myTree.map(_ + 1)

  // Exercise

  def main(args: Array[String]): Unit = {

    println(do10x(List(1, 2, 3)))
    println(do10x(Option(3)))
    println(do10x(Try(3)))

    //Exercise 2 - Create a shorter version of your do10x method using extension methods

    println(
      do10x[Tree](Branch(30, Leaf(10), Leaf(20))) // if we remove the Tree type then the compiler will look for a Functor[Branch]
    )


    // companion object way
    println(
      do10x(Tree.branch(30, Tree.leaf(10), Tree.leaf(20)))
      // using our companion object with smart constructors we can omit the [Tree] type from do10x
    )

    println(incrementedTree) // Branch(41,Branch(6,Leaf(11),Leaf(31)),Leaf(21))

  }
}
