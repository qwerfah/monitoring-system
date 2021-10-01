package com.qwerfah.generator.resources

import java.time.LocalDateTime

import com.qwerfah.common.Uid

final case class ParamValueRequest(
  paramUid: Uid,
  instanceUid: Uid,
  value: String
)

final case class ParamValueResponse(
  uid: Uid,
  paramUid: Uid,
  instanceUid: Uid,
  value: String,
  time: LocalDateTime
)
