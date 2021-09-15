package com.qwerfah.equipment

import io.circe.{Encoder, Json}

/** Provides json encoders for different respose types. */
object Encoders {
    def encodeErrorList(es: List[Exception]): Json = {
        val messages = es.map(x => Json.fromString(x.getMessage))
        Json.obj("errors" -> Json.arr(messages: _*))
    }

    /** Json encoder for error response type. */
    implicit val encodeException: Encoder[Exception] = Encoder.instance({
        case e: io.finch.Errors => encodeErrorList(e.errors.toList)
        case e: io.finch.Error =>
            e.getCause match {
                case e: io.circe.Errors => encodeErrorList(e.errors.toList)
                case err => Json.obj("message" -> Json.fromString(e.getMessage))
            }
        case e: Exception =>
            Json.obj("message" -> Json.fromString(e.getMessage))
    })
}
