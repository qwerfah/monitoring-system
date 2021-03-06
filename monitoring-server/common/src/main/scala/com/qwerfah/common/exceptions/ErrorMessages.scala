package com.qwerfah.common.exceptions

import com.qwerfah.common.Uid
import com.qwerfah.common.http.ServiceTag

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

case object InvalidTokenHeader extends ErrorMessage {
    override def getMessage = "Invalid authorization header provided."
}

case object InsufficientRole extends ErrorMessage {
    override def getMessage = "User have no rights to perform operation."
}

final case class NoInstance(uid: Uid) extends ErrorMessage {
    override def getMessage = s"Equipment instance with uid $uid not found."
}

final case class NoModel(uid: Uid) extends ErrorMessage {
    override def getMessage = s"Equipment model with uid $uid not found."
}

final case class BadModelRemove(uid: Uid) extends ErrorMessage {
    override def getMessage =
        s"Can't delete equipment model with uid $uid: one or more services in remove request chain return error.."
}

final case class NoParam(uid: Uid) extends ErrorMessage {
    override def getMessage = s"Equipment model param with uid $uid not found."
}

final case class BadParamRemove(uid: Uid) extends ErrorMessage {
    override def getMessage =
        s"Can't delete param with uid $uid: one or more services in remove request chain return error."
}

final case class NoParams(uids: Seq[Uid]) extends ErrorMessage {
    override def getMessage =
        s"Equipment model param(s) with uid(s) ${uids
            .fold("")((a, b) => a + ", " + b.toString)} not found."
}

final case class NoUser(uid: Uid) extends ErrorMessage {
    override def getMessage = s"User with uid $uid not found."
}

final case class DuplicateUser(login: String) extends ErrorMessage {
    override def getMessage = s"User with login $login already exists."
}

final case class NoMonitor(uid: Uid) extends ErrorMessage {
    override def getMessage = s"Monitor with uid $uid not found."
}

final case class NoInstanceMonitors(instanceUid: Uid) extends ErrorMessage {
    override def getMessage =
        s"Monitors for equipment instance with uid $instanceUid $instanceUid not found."
}

final case class NoMonitorParam(puid: Uid, muid: Uid) extends ErrorMessage {
    override def getMessage =
        s"Param with uid $puid not tracked by monitor with uid $muid."
}

final case class NoMonitorParams(muid: Uid) extends ErrorMessage {
    override def getMessage =
        s"Params tracked by monitor with uid $muid not found."
}

final case class NoParamTrackings(puid: Uid) extends ErrorMessage {
    override def getMessage =
        s"Trackings for param with uid $puid not found."
}

final case class NoOperationRecord(uid: Uid) extends ErrorMessage {
    override def getMessage =
        s"Service operation record with uid $uid not found."
}

final case class NoOperationRecords(serviceId: String) extends ErrorMessage {
    override def getMessage =
        s"No records about any $serviceId service operations found."
}

final case class NoFile(uid: Uid) extends ErrorMessage {
    override def getMessage = s"Documentation file with uid $uid not found."
}

final case class NoParamValue(uid: Uid) extends ErrorMessage {
    override def getMessage =
        s"Equipment parameter value with uid $uid not found."
}

final case class NoParamValuesForInstance(iuid: Uid) extends ErrorMessage {
    override def getMessage =
        s"Equipment parameter values of instance with uid $iuid not found."
}

final case class NoValuesForParam(puid: Uid) extends ErrorMessage {
    override def getMessage =
        s"Equipment parameter values for param with uid $puid not found."
}

final case class NoParamValues(puid: Uid, iuid: Uid) extends ErrorMessage {
    override def getMessage =
        s"Equipment parameter values of instance with uid $iuid for param with uid $puid not found."
}

case object NoMultipartData extends ErrorMessage {
    override def getMessage = "No file was presented in multipart data."
}

case object InvalidStatusCode extends ErrorMessage {
    override def getMessage = "Status code must be between 100 and 599."
}

final case class DuplicatedMonitorParam(puid: Uid, muid: Uid)
  extends ErrorMessage {
    override def getMessage =
        s"Param with uid $puid already tracked by monitor with uid $muid."
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

final case class ServiceUnavailable(service: ServiceTag) extends ErrorMessage {
    override def getMessage = s"$service service temporarily unavailable."
}

final case class ServiceInternalError(service: ServiceTag)
  extends ErrorMessage {
    override def getMessage = s"$service service internal error."
}

final case class BadServiceResult(service: ServiceTag) extends ErrorMessage {
    override def getMessage = s"$service service returns unprocessable result."
}

final case class UnknownServiceResponse(service: ServiceTag)
  extends ErrorMessage {
    override def getMessage =
        s"$service service returns response with unknown status code."
}

final case class InterserviceAuthFailed(service: ServiceTag)
  extends ErrorMessage {
    override def getMessage =
        s"Can't authorize in $service service with current service credentials."
}

case object NonexistentInstance extends ErrorMessage {
    override def getMessage =
        s"Attempted to add param values for instance that was removed during request processing."
}
