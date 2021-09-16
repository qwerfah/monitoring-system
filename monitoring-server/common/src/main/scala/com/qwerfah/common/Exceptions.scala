package com.qwerfah.common

import io.circe.schema.ValidationError

import cats.data.NonEmptyList
import com.qwerfah.common.ErrorMessages._

object Exceptions {

    final case class InvalidJsonBodyException(
      message: ErrorMessage,
      errors: NonEmptyList[ValidationError],
      cause: Throwable = None.orNull
    ) extends Exception(message.getMessage, cause)
}
