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

final case class UserRestored(uid: Uid) extends ResponseMessage {
    override val message = s"User with uid $uid restored."
}

final case class MonitorParamAdded(uid: Uid) extends ResponseMessage {
    override val message = s"Param tracking for monitor with uid $uid added."
}

final case class MonitorParamRemoved(puid: Uid, muid: Uid)
  extends ResponseMessage {
    override val message =
        s"Tracking for param with uid $puid for monitor with uid $muid removed."
}

final case class MonitorParamsRemoved(muid: Uid) extends ResponseMessage {
    override val message =
        s"All param trackings for for monitor with uid $muid removed."
}

final case class ParamTrackingsRemoved(puid: Uid) extends ResponseMessage {
    override val message =
        s"All trackings for for param with uid $puid removed."
}

final case class MonitorParamsRestored(muid: Uid) extends ResponseMessage {
    override val message =
        s"All param trackings for for monitor with uid $muid restored."
}

final case class ParamTrackingsRestored(puid: Uid) extends ResponseMessage {
    override val message =
        s"All trackings for for param with uid $puid restored."
}

final case class MonitorUpdated(uid: Uid) extends ResponseMessage {
    override val message = s"Monitor with uid $uid updated."
}

final case class MonitorRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Monitor with uid $uid removed."
}

final case class MonitorRestored(uid: Uid) extends ResponseMessage {
    override val message = s"Monitor with uid $uid restored."
}

final case class InstanceMonitorsRemoved(uid: Uid) extends ResponseMessage {
    override val message =
        s"Monitors for equipment instance with uid $uid removed."
}

final case class InstanceMonitorsRestored(uid: Uid) extends ResponseMessage {
    override val message =
        s"Monitors for equipment instance with uid $uid restored."
}

final case class RecordRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"Service operation record with uid $uid removed."
}

final case class RecordRestored(uid: Uid) extends ResponseMessage {
    override val message = s"Service operation record with uid $uid restored."
}

final case class RecordsRemoved(serviceId: String) extends ResponseMessage {
    override val message =
        s"All records about $serviceId service operations removed."
}

final case class RecordsRestored(serviceId: String) extends ResponseMessage {
    override val message =
        s"All records about $serviceId service operations restored."
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

final case class ParamValueRestored(uid: Uid) extends ResponseMessage {
    override val message = s"Parameter value with uid $uid restored."
}

final case class ParamValuesRemoved(uid: Uid) extends ResponseMessage {
    override val message = s"All values for parameter with uid $uid removed."
}

final case class ParamValuesRestored(uid: Uid) extends ResponseMessage {
    override val message = s"All values for parameter with uid $uid restored."
}

final case class InstanceParamValuesRemoved(uid: Uid) extends ResponseMessage {
    override val message =
        s"All parameter values for instance with uid $uid removed."
}

final case class InstanceParamValuesRestored(uid: Uid) extends ResponseMessage {
    override val message =
        s"All parameter values for instance with uid $uid restored."
}
