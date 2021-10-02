package com.qwerfah.generator.json

import java.time.LocalDateTime

import io.circe.{Decoder, HCursor}

import cats.implicits._
import cats.data.Validated._
import cats.data.{NonEmptyList, Validated, ValidatedNel}

import com.qwerfah.generator.resources._
import com.qwerfah.generator.json.JsonSchemas._

import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.http.HttpMethod

/** Provide custom json decoders with validation for different model resources.
  */
object Decoders {
    implicit val decodeParamValueRequest: Decoder[ParamValueRequest] =
        (c: HCursor) => {
            paramValueRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        paramUid <- c.downField("paramUid").as[Uid]
                        instanceUid <- c.downField("instanceUid").as[Uid]
                        value <- c.downField("value").as[String]
                    } yield ParamValueRequest(paramUid, instanceUid, value)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeParamValueResponse: Decoder[ParamValueResponse] =
        (c: HCursor) => {
            paramValueRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        uid <- c.downField("uid").as[Uid]
                        paramUid <- c.downField("paramUid").as[Uid]
                        instanceUid <- c.downField("instanceUid").as[Uid]
                        value <- c.downField("value").as[String]
                        time <- c.downField("time").as[LocalDateTime]
                    } yield ParamValueResponse(
                      uid,
                      paramUid,
                      instanceUid,
                      value,
                      time
                    )
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }
}
