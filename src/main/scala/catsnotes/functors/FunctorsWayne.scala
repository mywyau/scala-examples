package catsnotes.functors


class FunctorsWayne {

  // A => A
  val addOne: Int => Int =
    x => x + 1

  val partialFunc: PartialFunction[Int, Int] = {
    case x: Int => x + 1
  }

  def addOneMethod(x: Int) = x + 1

  def addOneMethod[A](x: A): A = x // parametric polymorphism

  val optFive: Option[Int] = Option(5)
  val optSeven: Option[Int] = Option(7)

  // F[A]
  val mappy: Option[Int] = optFive.map(addOne)
  val mappy2: Option[Int] = optFive.map(addOneMethod)

  val flatMappy: Option[Int] =
    optFive.flatMap(five =>
      optSeven.map(seven =>
        five + seven
      )
    )

  val flatMappy2: Option[Int] =
    for {
      five <- optFive
      seven <- optFive
    } yield five + seven

  val numbers = Seq(1, 2, 3, 4, 5)

  val matching = numbers match {
    case Seq(1, 2, 3, _, 5) => "regular sequence that matches anything in the 4th position"
    case i @ Seq(_) if i.last > 10 => "last number greater than 10"
    case _ =>
  }


}

