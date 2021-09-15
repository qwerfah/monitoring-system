package com.qwerfah.equipment.resources

final case class AddParamRequest(
  modelUid: Uid,
  name: String,
  measurmentUnits: Option[String]
)

final case class UpdateParamRequest(
  name: String,
  measurmentUnits: Option[String]
)

final case class ParamResponse(
  uid: Uid,
  modelUid: Uid,
  name: String,
  measurmentUnits: Option[String]
)
