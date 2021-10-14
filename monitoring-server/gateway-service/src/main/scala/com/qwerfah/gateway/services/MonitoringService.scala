package com.qwerfah.gateway.services

import com.twitter.finagle.http.Response

import com.qwerfah.monitoring.resources._

import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid

trait MonitoringService[F[_]] {
    def getMonitors: F[Response]

    def getInstanceMonitors(instanceUid: Uid): F[Response]

    def getMonitor(uid: Uid): F[Response]

    def getMonitorParams(uid: Uid): F[Response]

    def getMonitorParamValues(uid: Uid): F[Response]

    def addMonitor(instanceUid: Uid, request: AddMonitorRequest): F[Response]

    def addMonitorParams(monitorUid: Uid, request: MonitorParamsRequest): F[Response]

    def updateMonitor(uid: Uid, request: UpdateMonitorRequest): F[Response]

    def removeMonitor(uid: Uid): F[Response]

    def removeMonitorParam(monitorUid: Uid, paramUid: Uid): F[Response]
}
