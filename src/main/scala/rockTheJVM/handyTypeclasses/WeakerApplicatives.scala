package rockTheJVM.handyTypeclasses

import cats.{Functor, Semigroupal}

object WeakerApplicatives {

  trait MyApply[W[_]] extends Functor[W] with Semigroupal[W] {

    override def product[A, B](fa: W[A], fb: W[B]): W[(A, B)] = {
      val functionWrapper: W[B => (A, B)] = map(fa)(a => (b: B) => (a, b))
      ap(functionWrapper)(fb)
    }

    def mapN[A, B, C](tuple: (W[A], W[B]))(f: (A, B) => C): W[C] = {
      // my solution which sucks
      val (wa, wb) = tuple
      val wTupleAB = product(wa, wb)

      map(wTupleAB)(t => f(t._1, t._2))
    }

    def mapNSolution[A, B, C](tuple: (W[A], W[B]))(f: (A, B) => C): W[C] = {
      // video solution which  is kinda close tbh to my answer but much nicer
      val tupleWrapper = product(tuple._1, tuple._2)

      map(tupleWrapper) {
        case (a, b) => f(a, b)
      }
    }


    def ap[B, T](wf: W[B => T])(wb: W[B]): W[T]
  }


  trait MyApplicative[W[_]] extends Functor[W] with Semigroupal[W] {

    // so Applicative in cats only has .pure and extends Apply because the .product and .ap are auxiliary i.e. they are supplementary

    def pure[W[_], A](a: A): W[A] // fundamental method of an applicative

    //    override def product[A, B](fa: W[A], fb: W[B]): W[(A, B)] = {
    //      val functionWrapper: W[B => (A, B)] = map(fa)(a => (b: B) => (a, b))
    //      ap(functionWrapper)(fb)
    //    }
    //
    //    def ap[B, T](wf: W[B => T])(wb: W[B]): W[T] = ???
  }

  trait CatApplicative[W[_]] extends MyApply[W] {

    // notice it does not have .product or .ap since they can be obtained from MyApply or well Cat Apply
    // well it's basic trait extension

    def pure[W[_], A](a: A): W[A] // fundamental method of an applicative

    //    val apFromMyApply = ap()
    //    val productFromMyApply = product()
  }

  import cats.Apply
  import cats.instances.option._

  val applyOption = Apply[Option]
  val funcApp = applyOption.ap(Some((x: Int) => x + 1))(Some(2)) // Some(3) //rarely used this way

  //more useful extension methods

  import cats.syntax.apply._

  val tupleOfOptions: (Option[Int], Option[Int], Option[Int]) = (Option(1), Option(2), Option(3))
  val optionOfTuple: Option[(Int, Int, Int)] = tupleOfOptions.tupled // look how it reverses the tupling above //Some((1,2,3))
  val sumOption = tupleOfOptions.mapN(_ + _ + _) //Some(6)  // any arity up to 22 I think


  def main(args: Array[String]): Unit = {

  }

}
