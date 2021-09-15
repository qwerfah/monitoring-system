package com.qwerfah.equipment.resources

import enumeratum._

/** Equipment instance functioning status. */
sealed trait EquipmentStatus extends EnumEntry

case object EquipmentStatus
  extends Enum[EquipmentStatus]
  with CirceEnum[EquipmentStatus] {

    case object Active extends EquipmentStatus
    case object Inactive extends EquipmentStatus
    case object Decommissioned extends EquipmentStatus

    val values = findValues

}
