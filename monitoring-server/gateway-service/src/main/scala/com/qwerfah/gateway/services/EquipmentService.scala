package com.qwerfah.gateway.services

import com.twitter.finagle.http.Response

import com.qwerfah.equipment.resources._
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid

trait EquipmentService[F[_]] {
    def getModels: F[Response]

    def getModel(uid: Uid): F[Response]

    def addModel(request: ModelRequest): F[Response]

    def updateModel(uid: Uid, request: ModelRequest): F[Response]

    def removeModel(uid: Uid): F[Response]

    def getInstances: F[Response]

    def getModelInstances(modelUid: Uid): F[Response]

    def getInstance(uid: Uid): F[Response]

    def addInstance(modelUid: Uid, request: AddInstanceRequest): F[Response]

    def updateInstance(uid: Uid, request: UpdateInstanceRequest): F[Response]

    def removeInstance(uid: Uid): F[Response]

    def getParams: F[Response]

    def getModelParams(modelUid: Uid): F[Response]

    def getInstanceParams(instanceUid: Uid): F[Response]

    def getParam(uid: Uid): F[Response]

    def addParam(request: AddParamRequest): F[Response]

    def updateParam(uid: Uid, request: UpdateParamRequest): F[Response]

    def removeParam(uid: Uid): F[Response]
}
