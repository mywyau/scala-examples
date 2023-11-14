package topics.polymorphism

class Polymorph {

  /*
    We will pass in a generic type A that will be then matched on and extracted out based on the type passed into the method
    We need to explicitly do this for each type scenario.
  */

  def applyDiscount[A](discount: A): String = {
    discount match {
      case d: String => s"Lookup percentage discount in database for: $d"
      case d: Int => s"$d discount will be applied"
      case _ => "Unsupported discount type" // wild card to be safe ;)
    }
  }

  def returnGenericApplyDiscount[A](discount: A): Option[A] = {
    discount match {
      case d: String =>
        println(s"Lookup percentage discount in database for: $d")
        Option[A](discount)
      case d: Int =>
        println(s"$d discount will be applied")
        Option[A](discount)
      case _ =>
        println("Unsupported discount type")
        Option[A](discount)
    }
  }
}
