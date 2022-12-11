package rockTheJVM.dataManipulation

import rockTheJVM.dataManipulation.Evaluation.instantEval

object Evaluation {

  /*
    Cats makes the distinction between
  - evaluating an expression eagerly
  - evaluating lazily and everytime you request it
  - evaluating lazily and keeping the value (memoizing)
   */

  import cats.Eval

  val instantEval = Eval.now {
    //computed eagerly
    println("Computing now")
    64345
  }

  val redoEval: Eval[Int] = Eval.always {
    //does not println unless requested only everytime you access it
    println("Computing again")
    4234
  }

  val delayedEval = Eval.later {
    //later will run println inside once but keep the value, you will not see println a second time, but can access the value.
    println("Computing later")
    53278
  }

  val composedEvaluation = instantEval.flatMap(value1 => delayedEval.map(value2 => value1 + value2))

  val composedForEvaluation =
    for {
      value1 <- instantEval
      value2 <- delayedEval
    } yield value1 + value2

  val evalEx1 =
    for {
      a <- delayedEval
      b <- redoEval
      c <- instantEval
      d <- redoEval
    } yield a + b + c + d
  // now , later, again, agian , sum, again, again, sum

  // memoize
  // "remember" a computed value
  val dontRecompute: Eval[Int] = redoEval.memoize // hold the internal value and doens not need to recompute this value
  // can flatMap Evals

  //chains of evaluations that can me recomputed at will
  val tutorial =
    Eval.
      always {
        println("Step 1...");
        "put the guitar on your lap"
      }
      .map { step1 => println("Step 2"); "then put your left hand on the neck" }
      .memoize // remember the value up until this point
      .map { steps12 => println("Step 3, more complicated"); s"$steps12 then with the right hand strike the strings" }

  def defer[T](eval: => Eval[T]): Eval[T] =
    Eval.later(()).flatMap(_ => eval)

  def reverseList[T](list: List[T]): List[T] =
    if (list.isEmpty) list
    else reverseList(list.tail) :+ list.head

  def reverseEval[T](list: List[T]): Eval[List[T]] = { // not stack recursive so will stack overflow with large numbers
    if (list.isEmpty) Eval.now(list)
    else reverseEval(list.tail).map(_ :+ list.head)
  }

  def reverseEvalSafe[T](list: List[T]): Eval[List[T]] = {
    if (list.isEmpty) Eval.now(list)
    else defer(reverseEvalSafe(list.tail).map(_ :+ list.head))
    // because we are using Eval.later which is evaluated in a tail recursive way we are able to use it to perform a stack safe .reverseEval
    // this is because of the implementation of Eval.later we can chain Eval.later(v).flatmap  over and over essentially
  }


  def main(args: Array[String]): Unit = {

    println(
      defer(Eval.now {
        println("Now!")
        42
      }).value) // will only print 42 and the println("Now") to console only when .value is called

    //  println(reverseEval((1 to 10000).toList).value) //stack overflow
    println(reverseEval((1 to 100).toList).value)
    println(reverseEvalSafe((1 to 10000).toList).value)
  }
}
