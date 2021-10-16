package topics.polymorphism

import scala.reflect.ClassTag


/*
   There is a scenario you may want with custom types and only collect based on your custom enum, however Scala has type erasure at
   compile time so you need to wrap your values in a 'ClassTag'. Another concept is TypeTags look them up when needed.
   ClassTags can be a little dangerous when debugging as it the object type is overwritten and can be hidden, use only
   when necessary. If used safely then it should be fine, remember to document and make things clear that there is a ClassTag involved,
   and you are overriding the type erasure.
 */

sealed trait MonthBased {
  val somethingMonthBasedOnly = ???
}

sealed trait NovemberOnwards extends MonthBased {
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

object MonthBased {

  val allValues: Seq[MonthBased] = Seq(November, December, January, February, March)

  val notNovemberMonths: Seq[DecemberOnwards] = allValues.collect {
    case month: DecemberOnwards => month
  }
}