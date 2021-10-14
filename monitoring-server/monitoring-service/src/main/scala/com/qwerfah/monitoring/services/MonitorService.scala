package com.qwerfah.monitoring.services

import com.qwerfah.monitoring.models._
import com.qwerfah.monitoring.resources._

import com.qwerfah.equipment.resources._

import com.qwerfah.generator.resources._

import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid

trait MonitorService[F[_]] {

    /** Get all existing monitors from repository.
      * @return
      *   Monitors collection or error in case of failure.
      */
    def getMonitors: F[ServiceResponse[Seq[MonitorResponse]]]

    /** Get monitor by its uid.
      * @param uid
      *   Monitor uid.
      * @return
      *   Monitor with specified uid or error in case of failure.
      */
    def getMonitor(uid: Uid): F[ServiceResponse[MonitorResponse]]

    def getMonitorCount(uids: Seq[Uid]): F[ServiceResponse[Int]]

    /** Get all monitors for specified instance.
      * @param instanceUid
      *   Instance uid.
      * @return
      *   Collection of monitors associated with spicified instance.
      */
    def getInstanceMonitors(
      instanceUid: Uid
    ): F[ServiceResponse[Seq[MonitorResponse]]]

    /** Get uids of all instances that monitor by monitoring service.
      * @return
      *   Collection of all traked instances uids
      */
    def getMonitoringInstances: F[ServiceResponse[Seq[Uid]]]

    /** Get params tracked by specified monitor.
      * @param uid
      *   Monitor uid.
      * @return
      *   Collection of params tracked by specified monitor.
      */
    def getMonitorParams(
      uid: Uid
    ): F[ServiceResponse[Seq[ParamResponse]]]

    /** Get all values of all specified monitor params for all time. Request to
      * remote generator service.
      * @param uid
      *   Monitor identifier.
      * @return
      *   All values of all specified monitor params for all time
      */
    def getMonitorParamValues(
      uid: Uid
    ): F[ServiceResponse[Seq[ParamValueResponse]]]

    /** Add new monitor.
      * @param request
      *   New monitor.
      * @return
      *   New monitor instance added to storage.
      */
    def addMonitor(
      instanceUid: Uid,
      request: AddMonitorRequest
    ): F[ServiceResponse[MonitorResponse]]

    /** Add new param tracked by specified monitor.
      * @param param
      *   Pair monitor-param uid.
      * @return
      *   Message describes successful or failed operation.
      */
    def addMonitorParam(
      monitorUid: Uid,
      param: MonitorParamRequest
    ): F[ServiceResponse[ResponseMessage]]

    def addMonitorParams(
      monitorUid: Uid,
      params: MonitorParamsRequest
    ): F[ServiceResponse[ResponseMessage]]

    /** Update existing monitor data.
      * @param uid
      *   Monitor uid.
      * @param request
      *   New monitor data.
      * @return
      *   Message describes successful or failed operation.
      */
    def updateMonitor(
      uid: Uid,
      request: UpdateMonitorRequest
    ): F[ServiceResponse[ResponseMessage]]

    /** Remove monitor by its uid (soft delete).
      * @param uid
      *   Monitor uid.
      * @return
      *   Message describes successful or failed operation.
      */
    def removeMonitor(uid: Uid): F[ServiceResponse[ResponseMessage]]

    /** Remove all monitors for specified equipment instance (soft delete).
      * @param instanceUid
      *   Equipment instance identifier.
      * @return
      *   Message describes successful or failed operation.
      */
    def removeInstanceMonitors(
      instanceUid: Uid
    ): F[ServiceResponse[ResponseMessage]]

    /** Restore monitor if it wasn't removed permanently.
      * @param uid
      *   Monitor identifier.
      * @return
      *   Message describes successful or failed operation.
      */
    def restoreMonitor(uid: Uid): F[ServiceResponse[ResponseMessage]]

    /** Restore all equipment instance monitors that wasn't removed permanently.
      * @param instanceUid
      *   Equipment instance identifier.
      * @return
      *   Message describes successful or failed operation.
      */
    def restoreInstanceMonitors(
      instanceUid: Uid
    ): F[ServiceResponse[ResponseMessage]]

    /** Remove monitor param by monitor and param ids (soft delete).
      * @param monitorUid
      *   Monitor identifier.
      * @param paramUid
      *   Parameter identifier.
      * @return
      *   Message describes successful or failed operation.
      */
    def removeMonitorParam(
      monitorUid: Uid,
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]]

    /** Remove all tracked params for specified monitor (soft delete).
      * @param monitorUid
      *   Monitor identifier.
      * @return
      *   Message describes successful or failed operation.
      */
    def removeMonitorParamsForMonitor(
      monitorUid: Uid
    ): F[ServiceResponse[ResponseMessage]]

    /** Remove all trackers for specifier parameter (soft delete).
      * @param paramUid
      *   Parameter identifier.
      * @return
      *   Message describes successful or failed operation.
      */
    def removeMonitorParamsForParam(
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]]

    /** Restore monitor param by monitor and param ids if it wasn't removed
      * permanently.
      * @param monitorUid
      *   Monitor identifier.
      * @param paramUid
      *   Parameter identifier.
      * @return
      *   Message describes successful or failed operation.
      */
    def restoreMonitorParam(
      monitorUid: Uid,
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]]

    /** Restore all tracked params for specified monitor if it wasn't removed
      * permanently.
      * @param monitorUid
      *   Monitor identifier.
      * @return
      *   Message describes successful or failed operation.
      */
    def restoreMonitorParamsForMonitor(
      monitorUid: Uid
    ): F[ServiceResponse[ResponseMessage]]

    /** Restore all trackers for specifier parameter if it wasn't removed
      * permanently.
      * @param paramUid
      *   Parameter identifier.
      * @return
      *   Message describes successful or failed operation.
      */
    def restoreMonitorParamsForParam(
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]]
}
