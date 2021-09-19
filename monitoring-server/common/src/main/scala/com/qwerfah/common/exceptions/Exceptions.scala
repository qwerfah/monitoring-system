package com.qwerfah.common.exceptions

import io.circe.schema.ValidationError

import cats.data.NonEmptyList

import com.qwerfah.common.Uid

final case object BadRequestException extends Exception {
    override def getMessage = "One or more validation errors occured."
}

final case object DuplicateTokenException extends Exception {
    override def getMessage = "Token already in repository."
}

final case object NoTokenException extends Exception {
    override def getMessage = "Token is not presented in repository."
}

final case class NoInstanceException(uid: Uid) extends Exception {
    override def getMessage = s"Equipment instance with uid $uid not found."
}

final case class NoModelException(uid: Uid) extends Exception {
    override def getMessage = s"Equipment model with uid $uid not found."
}

final case class NoParamException(uid: Uid) extends Exception {
    override def getMessage = s"Equipment model param with uid $uid not found."
}

final case class NoUserException(uid: Uid) extends Exception {
    override def getMessage = s"User with uid $uid not found."
}

final case object InvalidCredentialsException extends Exception {
    override def getMessage = s"User with given credentials not found."
}

final case object NoTokenUserException extends Exception {
    override def getMessage = s"User for given token not found."
}

final case object InvalidTokenException extends Exception {
    override def getMessage = "Invalid authorizatiom token."
}

final case class InvalidJsonBodyException(
  errors: NonEmptyList[ValidationError],
  cause: Throwable = BadRequestException
) extends Exception(cause)
