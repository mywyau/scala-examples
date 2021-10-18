package rockTheJVM.abstractMath

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Monads {

  val numbersList = List(1, 2, 3)
  val charsList = List("a", "b", "c")

  val combinationsList = numbersList.flatMap(n => charsList.map(c => (n, c)))

  val combinationsListFor =
    for {
      n <- numbersList
      c <- charsList
    } yield (n, c)


  val numberOption = Option(2)
  val charOption = Option("d")

  val combinationOptionFor =
    for {
      n <- numberOption
      c <- charOption
    } yield (n, c)

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  val numberFuture = Future(42)
  val charFuture = Future("Z")

  val combinationFutureFor =
    for {
      n <- numberFuture
      c <- charFuture
    } yield (n, c)


  /*
   Pattern
   - wrapping a value into a M value (monadic value)
   - the flatMap mechanism

   Monad - higher kinded like Functor
   */

  trait MyMonad[M[_]] {

    def pure[A](value: A): M[A]

    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[A]
  }

  import cats.Monad
  import cats.instances.option._ //implicit Monad[Option]

  val optionMonad = Monad[Option]
  val anOption = Monad[Option].pure(4) //Option(4) == Some(4)
  val aTransformedOption = optionMonad.flatMap(anOption)(x => if (x % 3 == 0) Some(x + 1) else None)

  import cats.instances.list._

  val listMonad = Monad[List]
  val aList = Monad[List].pure(3)
  val aTransformedList = listMonad.flatMap(aList)(x => List(x, x + 1))

  val futureMonad = Monad[Future] // requires an execution context
  val aFuture = Monad[Future].pure(43)
  val aTransformedFuture = futureMonad.flatMap(aFuture)(x => Future(x + 44)) //Success(87)

  // specialized API

  def getPairsList(numbers: List[Int], chars: List[Char]): List[(Int, Char)] =
    numbers.flatMap(n => chars.map(c => (n, c)))

  def getPairsOption(oNumber: Option[Int], oChars: Option[Char]): Option[(Int, Char)] =
    oNumber.flatMap(n => oChars.map(c => (n, c)))

  def getPairsFuture(futureNumber: Future[Int], futureChars: Future[Char]): Future[(Int, Char)] =
    futureNumber.flatMap(n => futureChars.map(c => (n, c)))

  // a bit repetitive
  //Monads help generalise for the context

  def getPairs[M[_], A, B](ma: M[A], mb: M[B])(implicit monad: Monad[M]): M[(A, B)] = {
    monad.flatMap(ma)(a => monad.map(mb)(b => (a, b)))
  }


  def main(args: Array[String]): Unit = {

    //Look how powerful our api is, it is able to generalise over M[_]
    println(getPairs(numbersList, charsList))
    println(getPairs(numberOption, charOption))
    println(getPairs(numberFuture, charFuture))
  }

}
