package com.qwerfah.monitoring.json

import java.time.LocalDateTime

import io.circe.{Decoder, HCursor}

import cats.implicits._
import cats.data.Validated._
import cats.data.{NonEmptyList, Validated, ValidatedNel}

import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.http.HttpMethod

import com.qwerfah.monitoring.resources._
import com.qwerfah.monitoring.json.JsonSchemas._

/** Provide custom json decoders with validation for different model resources.
  */
object Decoders {
    implicit val decodeMonitorRequest: Decoder[MonitorRequest] = (c: HCursor) =>
        {
            monitorRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        instanceUid <- c.downField("instanceUid").as[Uid]
                        name <- c.downField("name").as[String]
                        description <- c
                            .downField("description")
                            .as[Option[String]]
                    } yield MonitorRequest(instanceUid, name, description)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeMonitorResponse: Decoder[MonitorResponse] =
        (c: HCursor) => {
            monitorRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        uid <- c.downField("uid").as[Uid]
                        instanceUid <- c.downField("instanceUid").as[Uid]
                        name <- c.downField("name").as[String]
                        description <- c
                            .downField("description")
                            .as[Option[String]]
                    } yield MonitorResponse(uid, instanceUid, name, description)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeMonitorParamRequest: Decoder[MonitorParamRequest] =
        (c: HCursor) => {
            monitorRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        paramUid <- c.downField("paramUid").as[Uid]
                    } yield MonitorParamRequest(paramUid)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeMonitorParamResponse: Decoder[MonitorParamResponse] =
        (c: HCursor) => {
            monitorRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        monitorUid <- c.downField("monitorUid").as[Uid]
                        paramUid <- c.downField("paramUid").as[Uid]
                    } yield MonitorParamResponse(monitorUid, paramUid)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }
}
