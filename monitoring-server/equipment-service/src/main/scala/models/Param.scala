package models

case class Param(
  id: Option[Int],
  uid: Guid,
  modelId: Int,
  name: String,
  measurmentUnits: Option[String]
)
