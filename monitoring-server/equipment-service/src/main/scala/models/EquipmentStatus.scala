package models

/** Equipment instance functioning status. */
object EquipmentStatus extends Enumeration {
    val Active = Value("Active")
    val Inactive = Value("Inactive")
    val Decommissioned = Value("Decommissioned")
}
