package topics.polymorphism

import scala.reflect.ClassTag

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

  /*
   There is a scenario you may want with custom types and only collect based on your custom enum, however Scala has type erasure at
   compile time so you need to wrap your values in a 'ClassTag'. Another concept is TypeTags look them up when needed.
   ClassTags can be a little dangerous when debugging as it the object type is overwritten and can be hidden, use only
   when necessary. If used safely then it should be fine, remember to document and make things clear that there is a ClassTag involved,
   and you are overriding the type erasure.
 */

  sealed trait SelectClaimPeriod {
    val somethingSelectClaimPeriodOnly = ???
  }

  sealed trait NovemberOnwards extends SelectClaimPeriod {
    val somethingNovemberOnwardsOnly = ???
  }

  case object November extends NovemberOnwards

  sealed trait DecemberOnwards extends NovemberOnwards {
    val somethingDecemberOnwardsOnly = ???
  }

  case object December extends DecemberOnwards

  sealed trait JanuaryOnwards extends DecemberOnwards {
    val somethingJanuaryOnwardsOnly = ???
  }

  case object January extends JanuaryOnwards

  case object February extends JanuaryOnwards

  case object March extends JanuaryOnwards


  val allValues: Seq[SelectClaimPeriod] = Seq(November, December, January, February, March)

  val notNovemberMonths: Seq[DecemberOnwards] = allValues.collect { case month: DecemberOnwards => month }

  def onwardsValues[T]()(implicit tag: ClassTag[T]): Seq[T] = {
    notNovemberMonths.collect[T, Seq[T]] { case month: T => month }
  }

}
