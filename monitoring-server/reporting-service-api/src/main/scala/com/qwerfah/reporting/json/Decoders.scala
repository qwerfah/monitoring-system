package com.qwerfah.reporting.json

import java.time.LocalDateTime

import io.circe.{Decoder, HCursor}

import cats.implicits._
import cats.data.Validated._
import cats.data.{NonEmptyList, Validated, ValidatedNel}

import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.http.HttpMethod

import com.qwerfah.reporting.resources._
import com.qwerfah.reporting.json.JsonSchemas._

/** Provide custom json decoders with validation for different model resources.
  */
object Decoders {
    implicit val decodeRecordRequest: Decoder[RecordRequest] = (c: HCursor) => {
        recordRequestSchema.validate(c.value) match {
            case Valid(()) =>
                for {
                    serviceId <- c.downField("serviceId").as[String]
                    route <- c.downField("route").as[String]
                    method <- c.downField("method").as[HttpMethod]
                    status <- c.downField("status").as[Int]
                    time <- c.downField("time").as[LocalDateTime]
                } yield RecordRequest(serviceId, route, method, status, time)
            case Invalid(errors) =>
                throw InvalidJsonBodyException(errors)
        }
    }

    implicit val decodeRecordResponse: Decoder[RecordResponse] = (c: HCursor) =>
        {
            recordRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        uid <- c.downField("uid").as[Uid]
                        serviceId <- c.downField("serviceId").as[String]
                        route <- c.downField("route").as[String]
                        method <- c.downField("method").as[HttpMethod]
                        status <- c.downField("status").as[Int]
                        time <- c.downField("time").as[LocalDateTime]
                    } yield RecordResponse(
                      uid,
                      serviceId,
                      route,
                      method,
                      status,
                      time
                    )
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }
}
