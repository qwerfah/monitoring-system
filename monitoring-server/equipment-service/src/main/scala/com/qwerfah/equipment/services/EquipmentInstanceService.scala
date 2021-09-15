package com.qwerfah.equipment.services

import com.qwerfah.equipment.resources._

trait EquipmentInstanceService[F[_]] {
    def get: F[ServiceResponse[Seq[InstanceResponse]]]
    def getById(id: Int): F[ServiceResponse[InstanceResponse]]
    def getByUid(uid: Uid): F[ServiceResponse[InstanceResponse]]
    def getByModelUid(modelUid: Uid): F[ServiceResponse[Seq[InstanceResponse]]]
    def add(instance: AddInstanceRequest): F[ServiceResponse[InstanceResponse]]
    def update(
      uid: Uid,
      instance: UpdateInstanceRequest
    ): F[ServiceResponse[String]]
    def removeById(id: Int): F[ServiceResponse[String]]
    def removeByUid(uid: Uid): F[ServiceResponse[String]]
}
