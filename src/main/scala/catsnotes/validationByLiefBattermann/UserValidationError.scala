package catsnotes.validationByLiefBattermann

sealed trait UserValidationError

case object NameNotValid extends UserValidationError

case object AgeOutOfRange extends UserValidationError

case object EmailNotValid extends UserValidationError

