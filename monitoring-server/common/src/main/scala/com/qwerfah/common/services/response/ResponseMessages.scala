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

final case class ModelRestored(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment model with uid $uid restored."
}

final case class InstanceUpdated(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment instance with uid $uid updated."
}

final case class InstanceRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment instance with uid $uid removed."
}

final case class InstanceRestored(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment instance with uid $uid restored."
}

final case class ParamUpdated(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment model param with uid $uid updated."
}

final case class ParamRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment model param with uid $uid removed."
}

final case class ParamRestored(uid: Uid) extends ResponseMessage {
    override val message = s"Equipment model param with uid $uid restored."
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

final case class RecordRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Service operation record with uid $uid updated."
}

final case class RecordsRemoved(serviceId: String) extends ResponseMessage {
    override val message =
        s"All records about $serviceId service operations removed."
}

final case class FileRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Documentation file with uid $uid removed."
}

final case class ModelFilesRemoved(uid: Uid) extends ResponseMessage {
    override val message =
        s"All documentation files for model with uid $uid removed."
}

final case class FileRestored(uid: Uid) extends ResponseMessage {
    override val message = s"Documentation file with uid $uid restored."
}

final case class ModelFilesRestored(uid: Uid) extends ResponseMessage {
    override val message =
        s"All documentation files for model with uid $uid restored."
}

case object ParamValuesAdded extends ResponseMessage {
    override val message =
        "Param values for all tracked equipment instances added."
}

final case class ParamValueRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Parameter value with uid $uid removed."
}

final case class ParamValuesRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"All values for parameter with uid $uid removed."
}

final case class InstanceParamValuesRemoved(uid: Uid) extends ResponseMessage {
    override val message =
        s"All parameter values for instance with uid $uid removed."
}
