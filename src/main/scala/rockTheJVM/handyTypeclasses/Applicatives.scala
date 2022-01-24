package rockTheJVM.handyTypeclasses

import cats.data.Validated

object Applicatives {

  // Extension of functors which gives functors the pure method

  import cats.Applicative

  // gives the ability to wrap a normal value into a wrapped value

  import cats.instances.list._

  val listApplicative = Applicative[List]

  val aList = listApplicative.pure(2) //List(2)

  import cats.instances.option._ // implicit Applicative[Option]

  val optionApplicative = Applicative[Option]

  val anOption = optionApplicative.pure(2) //Some(2)

  //functors are useful for generalising methods in the F[_] wrapper

  //pure extension method

  import cats.syntax.applicative._

  val aSweetList = 2.pure[List] //returns a List(2)
  val aSweetOption = 2.pure[Option] //returns a Some(2)

  //same pure method from monads

  //Monads extends Applicatives since they inherit the .pure() method
  // Applicatives are rarely used since we often use Monad or a more stronger useful type
  // Validated does not respect the monad laws but they do follow the Applicative functor laws for chaining with andThen

  type ErrorsOr[T] = Validated[List[String], T]

  val aValidValue: ErrorsOr[Int] = Validated.valid(43) // "pure"
  val aModifiedValidated = aValidValue.map(_ + 1) // map

  val validatedApplicative = Applicative[ErrorsOr]

  //TODO : thought experiment

  def apMikey[W[_], A, B](wf: W[A => B])(wa: W[A]): W[B] = ??? // already implemented but was needed for the exercise

  def productWithApplicativesMikey[W[_], A, B](wa: W[A], wb: W[B])(implicit applicative: Applicative[W]): W[(A, B)] = {
    // My implementation

    val f: W[A => (A, B)] = applicative.map(wb)(b => (_, b)) // you need a function for to be able to use ap within the W[_] wrapper
    val answer: W[(A, B)] = applicative.ap(f)(wa)

    answer
  }

  def productWithApplicatives[W[_], A, B](wa: W[A], wb: W[B])(implicit applicative: Applicative[W]): W[(A, B)] = {
    // video answer
    val functionWrapper: W[B => (A, B)] = applicative.map(wa)(a => (b: B) => (a, b))
    //    apMikey(functionWrapper)(wb)
    applicative.ap(functionWrapper)(wb)
  }

  // Applicatives have this .ap method, in the presence of this .ap method Applicatives can implement product from Semigroup
  // therefore Applicative the type extends Semigroupal

  // not really used on their own but used in cases for types that are not Monads e.g. Validated

  def main(args: Array[String]): Unit = {


  }
}
