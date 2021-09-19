package com.qwerfah.session.json

import io.circe.{Decoder, HCursor}

import cats.data.Validated._
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.implicits._

import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.session.resources._

object Decoders {
    import JsonSchemas._

    implicit val decodeUserRequest: Decoder[UserRequest] =
        (c: HCursor) => {
            userRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        login <- c.downField("login").as[String]
                        password <- c.downField("password").as[String]
                        role <- c.downField("role").as[UserRole]
                    } yield UserRequest(login, password, role)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeCredentials: Decoder[Credentials] =
        (c: HCursor) => {
            credentialsSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        login <- c.downField("login").as[String]
                        password <- c.downField("password").as[String]
                    } yield Credentials(login, password)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }
}
