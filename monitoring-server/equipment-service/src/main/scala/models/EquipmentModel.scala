package models

case class EquipmentModel(
  id: Option[Int],
  uid: Uid,
  name: String,
  description: Option[String]
)
