package com.qwerfah.equipment.resources

import com.qwerfah.common.Uid

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
