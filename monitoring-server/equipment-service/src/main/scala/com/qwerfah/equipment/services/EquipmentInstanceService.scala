package com.qwerfah.equipment.services

import com.qwerfah.equipment.resources._
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid

trait EquipmentInstanceService[F[_]] {
    def getAll: F[ServiceResponse[Seq[InstanceResponse]]]
    def get(uid: Uid): F[ServiceResponse[InstanceResponse]]
    def getByModelUid(modelUid: Uid): F[ServiceResponse[Seq[InstanceResponse]]]
    def add(
      modelUid: Uid,
      instance: AddInstanceRequest
    ): F[ServiceResponse[InstanceResponse]]
    def update(
      uid: Uid,
      instance: UpdateInstanceRequest
    ): F[ServiceResponse[ResponseMessage]]
    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]
    def restore(uid: Uid): F[ServiceResponse[ResponseMessage]]
}
