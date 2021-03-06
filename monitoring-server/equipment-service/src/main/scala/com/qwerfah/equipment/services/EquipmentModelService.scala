package com.qwerfah.equipment.services

import com.twitter.finagle.http.{Request, Response}

import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._

import com.qwerfah.documentation.resources.FileMetaResponse

import com.qwerfah.common.Uid
import com.qwerfah.common.services.response._

trait EquipmentModelService[F[_]] {
    def getAll: F[ServiceResponse[Seq[ModelResponse]]]
    def get(uid: Uid): F[ServiceResponse[ModelResponse]]
    def getFiles(uid: Uid): F[ServiceResponse[FileMetaResponse]]
    def add(model: ModelRequest): F[ServiceResponse[ModelResponse]]
    def addFile(uid: Uid, request: Request): F[Response]
    def update(
      uid: Uid,
      model: ModelRequest
    ): F[ServiceResponse[ResponseMessage]]
    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]
    def restore(uid: Uid): F[ServiceResponse[ResponseMessage]]
}
