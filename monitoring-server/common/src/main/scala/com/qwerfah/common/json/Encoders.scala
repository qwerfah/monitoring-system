package com.qwerfah.common.json

import io.circe.{Encoder, Json}

import scala.language.reflectiveCalls

import com.qwerfah.common.exceptions._
import com.qwerfah.common.services.response.ResponseMessage

/** Provide json encoders for different respose types. */
object Encoders {
    def encodeErrorList(es: Seq[Throwable]): Json = {
        val messages = es.map(x => Json.fromString(x.getMessage))
        Json.obj("errors" -> Json.arr(messages: _*))
    }

    implicit val encodeResponseMessage: Encoder[ResponseMessage] =
        Encoder.instance({ message =>
            Json.obj("message" -> Json.fromString(message.message))
        })

    /** Json encoder for error response type. */
    implicit val encodeException: Encoder[Exception] = Encoder.instance({
        case e: io.finch.Errors => encodeErrorList(e.errors.toList)
        case e: io.finch.Error =>
            e.getCause match {
                case e: io.circe.Errors => encodeErrorList(e.errors.toList)
                case err =>
                    Json.obj("message" -> Json.fromString(e.getMessage))
            }
        case e: io.circe.Error =>
            e.getCause match {
                case e: io.circe.Errors => encodeErrorList(e.errors.toList)
                case err =>
                    Json.obj("message" -> Json.fromString(e.getMessage))
            }
        case e: InvalidJsonBodyException =>
            Json.obj(
              "message" -> Json.fromString(e.getMessage),
              "errors" -> Json.arr(
                e.errors.toList.map(x => Json.fromString(x.getMessage)): _*
              )
            )
        case e: Exception =>
            Json.obj("message" -> Json.fromString(e.getMessage))
    })
}
