package categoryTheoryAndCats.applicativeFunctors.liefBattermann

sealed trait UserValidationError

case object NameNotValid extends UserValidationError

case object AgeOutOfRange extends UserValidationError

case object EmailNotValid extends UserValidationError

