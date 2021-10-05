package com.qwerfah.generator.services

import com.qwerfah.generator.resources._

import com.qwerfah.common.Uid
import com.qwerfah.common.services.response.{ServiceResponse, ResponseMessage}

trait ParamValueService[F[_]] {
    def get: F[ServiceResponse[Seq[ParamValueResponse]]]
    def get(uid: Uid): F[ServiceResponse[ParamValueResponse]]
    def get(
      paramUid: Option[Uid],
      instanceUid: Option[Uid]
    ): F[ServiceResponse[Seq[ParamValueResponse]]]
    def getLast(
      paramUid: Uid,
      instanceUid: Uid
    ): F[ServiceResponse[ParamValueResponse]]

    def add(request: ParamValueRequest): F[ServiceResponse[ParamValueResponse]]
    
    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]
    def removeByParamUid(paramUid: Uid): F[ServiceResponse[ResponseMessage]]
    def removeByInstanceUid(
      instanceUid: Uid
    ): F[ServiceResponse[ResponseMessage]]

    def restore(uid: Uid): F[ServiceResponse[ResponseMessage]]
    def restoreByParamUid(paramUid: Uid): F[ServiceResponse[ResponseMessage]]
    def restoreByInstanceUid(
      instanceUid: Uid
    ): F[ServiceResponse[ResponseMessage]]
}
