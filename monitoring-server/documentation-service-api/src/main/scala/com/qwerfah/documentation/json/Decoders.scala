package com.qwerfah.documentation.json

import io.circe.{Decoder, HCursor}

import cats.data.Validated._
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.implicits._

import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.documentation.resources._
import JsonSchemas._

/** Provide custom json decoders with validation for different model resources.
  */
object Decoders {
    implicit val decodeFileMetaResponse: Decoder[FileMetaResponse] =
        (c: HCursor) => {
            fileMetaRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        uid <- c.downField("uid").as[Uid]
                        modelUid <- c.downField("modelUid").as[Uid]
                        filename <- c.downField("filename").as[String]
                        contentType <- c.downField("contentType").as[String]
                    } yield FileMetaResponse(
                      uid,
                      modelUid,
                      filename,
                      contentType
                    )
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }
}
