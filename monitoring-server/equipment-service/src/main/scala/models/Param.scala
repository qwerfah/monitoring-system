package models

case class Param(
  id: Option[Int],
  uid: Uid,
  modelId: Int,
  name: String,
  measurmentUnits: Option[String]
)
