package com.qwerfah.gateway.json

import io.circe.{Decoder, HCursor}

import cats.data.Validated.{Valid, Invalid}

import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.resources._
import com.qwerfah.common.models.Token

object Decoders {
    import JsonSchemas._

    implicit val decodeUserRequest: Decoder[Token] =
        (c: HCursor) => {
            tokenSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        access <- c.downField("access").as[String]
                        refresh <- c.downField("refresh").as[String]
                    } yield Token(access, refresh)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }
}
