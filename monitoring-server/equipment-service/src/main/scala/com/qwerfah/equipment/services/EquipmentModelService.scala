package com.qwerfah.equipment.services

import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid

trait EquipmentModelService[F[_]] {
    def getAll: F[ServiceResponse[Seq[ModelResponse]]]
    def get(uid: Uid): F[ServiceResponse[ModelResponse]]
    def add(model: ModelRequest): F[ServiceResponse[ModelResponse]]
    def update(
      uid: Uid,
      model: ModelRequest
    ): F[ServiceResponse[ResponseMessage]]
    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]
    def restore(uid: Uid): F[ServiceResponse[ResponseMessage]]
}
