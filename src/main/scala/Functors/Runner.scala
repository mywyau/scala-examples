package Functors

object Runner extends App {

  val a = new FunctorExercise
  val optionalFive = Option(5)
  val addSix: Int => Int = i => i + 6

  val useMyFunctor = a.functorForOption.myVersionOfMap(optionalFive)(addSix)



}
