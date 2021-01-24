package Topics.Polymorphism

class Polymorph {

  def applyDiscount[A](discount: A): String = {
    discount match {
      case d: String => s"Lookup percentage discount in database for: $d"
      case d: Int => s"$d discount will be applied"
      case _ => "Unsupported discount type"
    }
  }

  def returnGenericApplyDiscount[A](discount: A): Option[A] = {
    discount match {
      case d: String =>
        s"Lookup percentage discount in database for: $d"
        Option[A](discount)
      case d: Int =>
        s"$d discount will be applied"
        Option[A](discount)
      case _ =>
        "Unsupported discount type"
        Option[A](discount)
    }
  }


}
