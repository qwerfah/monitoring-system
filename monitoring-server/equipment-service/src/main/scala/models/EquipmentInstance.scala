package models

import slick.lifted.Tag
import slick.model.Table

case class EquipmentInstance(
  id: Option[Int],
  uid: Guid,
  modelId: Int,
  name: String,
  description: Option[String],
  status: EquipmentStatus
)
