package com.qwerfah.common.json

import io.circe.{Decoder, HCursor}

import cats.data.Validated.{Valid, Invalid}

import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.resources._
import com.qwerfah.common.models.{Payload, Token}
import com.qwerfah.common.services.response.ResponseMessage

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

    implicit val decodeToken: Decoder[Token] =
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

    implicit val decodeUserResponse: Decoder[UserResponse] =
        (c: HCursor) => {
            userRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        uid <- c.downField("uid").as[Uid]
                        login <- c.downField("login").as[String]
                        role <- c.downField("role").as[UserRole]
                        token <- c.downField("token").as[Option[Token]]
                    } yield UserResponse(uid, login, role, token)
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

    implicit val decodePayload: Decoder[Payload] =
        (c: HCursor) => {
            payloadSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        uid <- c.downField("uid").as[Uid]
                        login <- c.downField("login").as[String]
                        role <- c.downField("role").as[UserRole]
                    } yield Payload(uid, login, role)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeResponseMessage: Decoder[ResponseMessage] =
        (c: HCursor) => {
            responseMessageSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        msg <- c.downField("message").as[String]
                    } yield new ResponseMessage { override val message = msg }
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

}
