
sealed trait GBOrEU

case object GB extends GBOrEU
case object EU extends GBOrEU


sealed trait MovementType

sealed trait UKMovementType

sealed trait EUMovementType

sealed trait Consignee

sealed trait  ImportExport

sealed trait UserType

//case class DeliveryPlaceTrader(movementType: MovementType, destinationType: DestinationType) extends UserType
//case class ComplementConsigneeTrader(movementType: MovementType, destinationType: DestinationType) extends UserType
//case class ConsigneeTrader(movementType: MovementType, destinationType: DestinationType) extends UserType
//case class PlaceOfDispatchTrader(movementType: MovementType, destinationType: DestinationType) extends UserType
//case class deliveryPlaceCustomsOffice(movementType: MovementType, destinationType: DestinationType) extends UserType

case object UkToUk extends MovementType with UKMovementType

case object ImportUK extends MovementType with UKMovementType


case object UkToEU extends MovementType  with EUMovementType

case object ImportEU extends MovementType with EUMovementType


case object DirectExport extends MovementType with ImportExport

case object IndirectExport extends MovementType with ImportExport

case object ImportDirectExport extends MovementType with ImportExport

case object ImportIndirectExport extends MovementType with ImportExport


sealed trait DestinationType

case object TaxWarehouse extends DestinationType

case object RegisteredConsignee extends DestinationType with Consignee

case object TemporaryRegisteredConsignee extends DestinationType with Consignee

case object ExemptedOrganisations extends DestinationType

case object DirectDelivery extends DestinationType

case object Export extends DestinationType

case object UnknownDestination extends DestinationType


object Ladon extends App {

  def test(movementType: MovementType, destinationType: DestinationType) = {
    ((movementType, destinationType) match {
      case (UkToUk | ImportUK, _) | (UkToEU | ImportEU, TaxWarehouse) =>
        Some("deliveryPlaceTrader")
      case (UkToEU | ImportEU, ExemptedOrganisations) =>
        Some("complementConsigneeTrader")
      case (UkToEU, RegisteredConsignee | TemporaryRegisteredConsignee | DirectDelivery | Export) |
           (ImportEU,  RegisteredConsignee | TemporaryRegisteredConsignee | DirectDelivery) =>
        Some("consigneeTrader")
      case (UkToEU, UnknownDestination) =>
        Some("placeOfDispatchTrader")
      case (DirectExport | IndirectExport | ImportDirectExport | ImportIndirectExport, _) =>
        Some("UPPERCASE")
      case _ =>
        Some("GB")
    }).getOrElse(GB)
  }


  def test2(movementType: MovementType, destinationType: DestinationType) = {

    ((movementType, destinationType) match {
      case (_: UKMovementType, _) | (_: EUMovementType, TaxWarehouse) => Some("deliveryPlaceTrader")
      case (_: EUMovementType, ExemptedOrganisations) => Some("complementConsigneeTrader")
      case (UkToEU, _: Consignee | DirectDelivery | Export) |
           (ImportEU, _: Consignee | DirectDelivery) => Some("consigneeTrader")
      case (UkToEU, UnknownDestination) => Some("placeOfDispatchTrader")
      case (_: ImportExport, _) => Some("UPPERCASE")
      case _ => Some(GB)
    }).getOrElse(GB)
  }

  println(test2(UkToUk, TaxWarehouse))
  println(test2(UkToEU, TaxWarehouse))
  println(test2(UkToEU, ExemptedOrganisations))
  println(test2(UkToEU, RegisteredConsignee))
  println(test2(UkToEU, UnknownDestination))
  println(test2(IndirectExport, UnknownDestination))
  println(test2(ImportIndirectExport, TaxWarehouse))
  println(test2(ImportIndirectExport, TaxWarehouse))

}