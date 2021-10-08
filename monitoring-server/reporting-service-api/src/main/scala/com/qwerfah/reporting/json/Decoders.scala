package com.qwerfah.reporting.json

import java.time.LocalDateTime

import io.circe.{Decoder, HCursor}

import cats.implicits._
import cats.data.Validated._
import cats.data.{NonEmptyList, Validated, ValidatedNel}

import com.qwerfah.reporting.json.JsonSchemas._

import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.http.HttpMethod
import com.qwerfah.common.resources.{RecordRequest, RecordResponse}

/** Provide custom json decoders with validation for different model resources.
  */
object Decoders {
    implicit val decodeRecordRequest: Decoder[RecordRequest] = (c: HCursor) => {
        recordRequestSchema.validate(c.value) match {
            case Valid(()) =>
                for {
                    userName <- c.downField("userName").as[Option[String]]
                    serviceId <- c.downField("serviceId").as[String]
                    route <- c.downField("route").as[String]
                    method <- c.downField("method").as[HttpMethod]
                    status <- c.downField("status").as[Int]
                    elapsed <- c.downField("elapsed").as[Long]
                    time <- c.downField("time").as[LocalDateTime]
                } yield RecordRequest(
                  userName,
                  serviceId,
                  route,
                  method,
                  status,
                  elapsed,
                  time
                )
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
                        userName <- c.downField("userName").as[Option[String]]
                        serviceId <- c.downField("serviceId").as[String]
                        route <- c.downField("route").as[String]
                        method <- c.downField("method").as[HttpMethod]
                        status <- c.downField("status").as[Int]
                        elapsed <- c.downField("elapsed").as[Long]
                        time <- c.downField("time").as[LocalDateTime]
                    } yield RecordResponse(
                      uid,
                      userName,
                      serviceId,
                      route,
                      method,
                      status,
                      elapsed,
                      time
                    )
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }
}
