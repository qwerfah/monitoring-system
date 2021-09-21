package com.qwerfah.common.exceptions

import com.qwerfah.common.Uid

sealed abstract class ErrorMessage extends Exception

case object InvalidBody extends ErrorMessage {
    override def getMessage = "One or more validation errors occured."
}

case object DuplicateToken extends ErrorMessage {
    override def getMessage = "Token already in repository."
}

case object NoToken extends ErrorMessage {
    override def getMessage = "Token is not presented in repository."
}

case object NoTokenHeader extends ErrorMessage {
    override def getMessage = "No authorization header provided."
}

final case class NoInstance(uid: Uid) extends ErrorMessage {
    override def getMessage = s"Equipment instance with uid $uid not found."
}

final case class NoModel(uid: Uid) extends ErrorMessage {
    override def getMessage = s"Equipment model with uid $uid not found."
}

final case class NoParam(uid: Uid) extends ErrorMessage {
    override def getMessage = s"Equipment model param with uid $uid not found."
}

final case class NoUser(uid: Uid) extends ErrorMessage {
    override def getMessage = s"User with uid $uid not found."
}

case object InvalidCredentials extends ErrorMessage {
    override def getMessage = s"User with given credentials not found."
}

case object NoTokenUser extends ErrorMessage {
    override def getMessage = s"User for given token not found."
}

case object InvalidToken extends ErrorMessage {
    override def getMessage = "Invalid authorizatiom token."
}

case object ExpiredToken extends ErrorMessage {
    override def getMessage = "Attempted to access with expired token."
}

case object NoExpiredToken extends ErrorMessage {
    override def getMessage = "Attempted to delete non-existent expired token."
}
