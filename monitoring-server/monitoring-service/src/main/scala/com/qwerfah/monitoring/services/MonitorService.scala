package com.qwerfah.monitoring.services

import com.qwerfah.monitoring.models._
import com.qwerfah.monitoring.resources._
import com.qwerfah.equipment.resources._
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid

trait MonitorService[F[_]] {

    /** Get all existing monitors from repository.
      * @return
      *   Monitors collection or error in case of failure.
      */
    def get: F[ServiceResponse[Seq[MonitorResponse]]]

    /** Get monitor by its uid.
      * @param uid
      *   Monitor uid.
      * @return
      *   Monitor with specified uid or error in case of failure.
      */
    def get(uid: Uid): F[ServiceResponse[MonitorResponse]]

    /** Get all monitors for specified instance.
      * @param instanceUid
      *   Instance uid.
      * @return
      *   Collection of monitors associated with spicified instance.
      */
    def getByInstanceUid(
      instanceUid: Uid
    ): F[ServiceResponse[Seq[MonitorResponse]]]

    /** Get params tracked by specified monitor.
      * @param uid
      *   Monitor uid.
      * @return
      *   Collection of params tracked by specified monitor.
      */
    def getParams(uid: Uid): F[ServiceResponse[Seq[ParamResponse]]]

    /** Add new monitor.
      * @param request
      *   New monitor.
      * @return
      *   New monitor instance added to storage.
      */
    def add(request: MonitorRequest): F[ServiceResponse[MonitorResponse]]

    /** Add new param tracked by specified monitor.
      * @param param
      *   Pair monitor-param uid.
      * @return
      *   Message describes successful or failed operation.
      */
    def addParam(monitorUid: Uid, param: MonitorParamRequest): F[ServiceResponse[ResponseMessage]]

    /** Update existing monitor data.
      * @param uid
      *   Monitor uid.
      * @param request
      *   New monitor data.
      * @return
      *   Message describes successful or failed operation.
      */
    def update(
      uid: Uid,
      request: MonitorRequest
    ): F[ServiceResponse[ResponseMessage]]

    /** Remove monitor by its uid.
      * @param uid
      *   Monitor uid.
      * @return
      *   Message describes successful or failed operation.
      */
    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]

    /** Remove tracked param from specified monitor.
      * @param param
      *   Pair monitor-param uid.
      * @return
      *   Message describes successful or failed operation.
      */
    def removeParam(monitorUid: Uid, paramUid: Uid): F[ServiceResponse[ResponseMessage]]
}
