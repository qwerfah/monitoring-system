package com.qwerfah.reporting.resources

import java.time.LocalDateTime

import com.qwerfah.common.Uid
import com.qwerfah.common.http.HttpMethod

final case class RecordRequest(
  serviceId: String,
  route: String,
  method: HttpMethod,
  status: Int,
  time: LocalDateTime
)

final case class RecordResponse(
  uid: Uid,
  serviceId: String,
  route: String,
  method: HttpMethod,
  status: Int,
  time: LocalDateTime
)
