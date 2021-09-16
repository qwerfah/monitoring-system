package com.qwerfah.equipment.services

import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.common.Uid

trait ParamService[F[_]] {
    def get: F[ServiceResponse[Seq[ParamResponse]]]
    def getById(id: Int): F[ServiceResponse[ParamResponse]]
    def getByUid(uid: Uid): F[ServiceResponse[ParamResponse]]
    def getByModelUid(modelUid: Uid): F[ServiceResponse[Seq[ParamResponse]]]
    def add(param: AddParamRequest): F[ServiceResponse[ParamResponse]]
    def update(uid: Uid, param: UpdateParamRequest): F[ServiceResponse[String]]
    def removeById(id: Int): F[ServiceResponse[String]]
    def removeByUid(uid: Uid): F[ServiceResponse[String]]
}
