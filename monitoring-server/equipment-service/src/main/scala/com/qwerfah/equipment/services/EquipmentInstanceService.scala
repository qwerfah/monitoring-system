package com.qwerfah.equipment.services

import com.qwerfah.equipment.resources._

trait EquipmentInstanceService[F[_]] {
    def get: F[ServiceResponse[Seq[InstanceResponse]]]
    def getById(id: Int): F[ServiceResponse[InstanceResponse]]
    def getByGuid(uid: Uid): F[ServiceResponse[InstanceResponse]]
    def add(instance: AddInstanceRequest): F[ServiceResponse[InstanceResponse]]
    def update(
      uid: Uid,
      instance: UpdateInstanceRequest
    ): F[ServiceResponse[String]]
    def removeById(id: Int): F[ServiceResponse[String]]
    def removeByGuid(uid: Uid): F[ServiceResponse[String]]
}