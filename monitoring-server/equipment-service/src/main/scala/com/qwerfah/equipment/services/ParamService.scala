package com.qwerfah.equipment.services

import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid

trait ParamService[F[_]] {
    def getAll: F[ServiceResponse[Seq[ParamResponse]]]
    def get(uid: Uid): F[ServiceResponse[ParamResponse]]
    def getByModelUid(modelUid: Uid): F[ServiceResponse[Seq[ParamResponse]]]
    def getByInstanceUid(
      instanceUid: Uid
    ): F[ServiceResponse[Seq[ParamResponse]]]
    def add(modelUid: Uid, param: AddParamRequest): F[ServiceResponse[ParamResponse]]
    def update(
      uid: Uid,
      param: UpdateParamRequest
    ): F[ServiceResponse[ResponseMessage]]
    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]
    def restore(uid: Uid): F[ServiceResponse[ResponseMessage]]
}
