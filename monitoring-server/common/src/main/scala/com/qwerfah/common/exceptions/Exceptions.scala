package com.qwerfah.common.exceptions

import io.circe.schema.ValidationError

import cats.data.NonEmptyList

import com.qwerfah.common.Uid

final case class InvalidJsonBodyException(
  errors: NonEmptyList[ValidationError],
  message: ErrorMessage = InvalidBody
) extends Exception(message.getMessage)
