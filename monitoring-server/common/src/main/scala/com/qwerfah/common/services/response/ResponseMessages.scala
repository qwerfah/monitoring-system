package com.qwerfah.common.services.response

import com.qwerfah.common.Uid

sealed trait ResponseMessage {
    val message: String
}

final case class ModelUpdated(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment model with uid $uid updated."
}

final case class ModelRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment model with uid $uid removed."
}

final case class InstanceUpdated(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment instance with uid $uid updated."
}

final case class InstanceRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment instance with uid $uid removed."
}

final case class ParamUpdated(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment model param with uid $uid updated."
}

final case class ParamRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment model param with uid $uid removed."
}

final case class UserUpdated(uid: Uid) extends ResponseMessage {
    override val message = s"User with uid $uid updated."
}

final case class UserRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"User with uid $uid removed."
}

final case class MonitorParamAdded(uid: Uid) extends ResponseMessage {
    override val message = s"Param tracking for monitor with uid $uid added."
}

final case class MonitorParamRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Param tracking for monitor with uid $uid removed."
}

final case class MonitorUpdated(uid: Uid) extends ResponseMessage {
    override val message = s"Monitor with uid $uid updated."
}

final case class MonitorRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Monitor with uid $uid removed."
}
