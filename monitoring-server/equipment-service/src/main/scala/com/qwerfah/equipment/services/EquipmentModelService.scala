package com.qwerfah.equipment.services

import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._

trait EquipmentModelService[F[_]] {
    def get: F[ServiceResponse[Seq[ModelResponse]]]
    def getById(id: Int): F[ServiceResponse[ModelResponse]]
    def getByUid(uid: Uid): F[ServiceResponse[ModelResponse]]
    def add(model: ModelRequest): F[ServiceResponse[ModelResponse]]
    def update(uid: Uid, model: ModelRequest): F[ServiceResponse[String]]
    def removeById(id: Int): F[ServiceResponse[String]]
    def removeByUid(uid: Uid): F[ServiceResponse[String]]
}
