package com.qwerfah.monitoring.json

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
    implicit val decodeAddMonitorRequest: Decoder[AddMonitorRequest] =
        (c: HCursor) => {
            addMonitorRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        name <- c.downField("name").as[String]
                        description <- c
                            .downField("description")
                            .as[Option[String]]
                        params <- c.downField("params").as[Seq[Uid]]
                    } yield AddMonitorRequest(name, description, params)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeUpdateMonitorRequest: Decoder[UpdateMonitorRequest] =
        (c: HCursor) => {
            updateMonitorRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        name <- c.downField("name").as[String]
                        description <- c
                            .downField("description")
                            .as[Option[String]]
                    } yield UpdateMonitorRequest(name, description)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeMonitorResponse: Decoder[MonitorResponse] =
        (c: HCursor) => {
            monitorResponseSchema.validate(c.value) match {
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
            monitorParamRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        paramUid <- c.downField("paramUid").as[Uid]
                    } yield MonitorParamRequest(paramUid)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeMonitorParamsRequest: Decoder[MonitorParamsRequest] =
        (c: HCursor) => {
            monitorParamsRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        params <- c.downField("params").as[Seq[Uid]]
                    } yield MonitorParamsRequest(params)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }
}
