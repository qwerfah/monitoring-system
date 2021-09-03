package models

case class EquipmentModel(
  id: Option[Int],
  uid: Guid,
  name: String,
  description: Option[String]
)
