package com.qwerfah.common.resources

import enumeratum._

sealed trait UserRole extends EnumEntry

/** System yser role. */
case object UserRole extends Enum[UserRole] with CirceEnum[UserRole] {

    /** Sysytem administrator (access to all operations). */
    case object SystemAdmin extends UserRole

    /** Equipment administrator (access to all equipment data manipulation
      * operations).
      */
    case object EquipmentAdmin extends UserRole

    /** Equipment user (access only to equipment data retrieving operations). */
    case object EquipmentUser extends UserRole

    /** External system service. */
    case object Service extends UserRole

    val values = findValues

}
