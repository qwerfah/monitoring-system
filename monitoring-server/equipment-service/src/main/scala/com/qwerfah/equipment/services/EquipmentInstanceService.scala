package com.qwerfah.equipment.services

import com.qwerfah.equipment.resources._

import com.qwerfah.monitoring.resources.AddMonitorRequest

import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid

trait EquipmentInstanceService[F[_]] {
    def getAll(
      modelUid: Option[Uid] = None,
      status: Option[EquipmentStatus] = None
    ): F[ServiceResponse[Seq[InstanceResponse]]]
    def get(uid: Uid): F[ServiceResponse[InstanceResponse]]
    def getMonitors(): F[ServiceResponse[Seq[InstanceMonitorResponse]]]
    def getMonitors(uid: Uid): F[ServiceResponse[Seq[InstanceMonitorResponse]]]

    def add(
      modelUid: Uid,
      instance: AddInstanceRequest
    ): F[ServiceResponse[InstanceResponse]]
    def addMonitor(
      instanceUid: Uid,
      request: AddMonitorRequest
    ): F[ServiceResponse[InstanceMonitorResponse]]
    def update(
      uid: Uid,
      instance: UpdateInstanceRequest
    ): F[ServiceResponse[ResponseMessage]]

    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]
    def restore(uid: Uid): F[ServiceResponse[ResponseMessage]]
}
