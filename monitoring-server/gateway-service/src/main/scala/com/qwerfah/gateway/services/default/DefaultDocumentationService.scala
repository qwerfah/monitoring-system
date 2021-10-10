package com.qwerfah.gateway.services.default

import cats.Monad
import cats.implicits._

import com.twitter.util.{Future, FuturePool}
import com.twitter.finagle.http.{Status, Method}
import com.twitter.finagle.http.{Request, Response}

import com.qwerfah.gateway.services.DocumentationService

import com.qwerfah.documentation.resources.FileRequest

import com.qwerfah.common.Uid
import com.qwerfah.common.http.HttpClient
import com.qwerfah.common.http.HttpMethod

class DefaultDocumentationService[F[_]: Monad](
  equipmentClient: HttpClient[F],
  documentationClient: HttpClient[F]
) extends DocumentationService[F] {

    override def getFiles: F[Response] =
        documentationClient.send(HttpMethod.Get, "/api/files")

    override def getModelFiles(modelUid: Uid): F[Response] =
        documentationClient.send(HttpMethod.Get, s"/api/models/$modelUid/files")

    override def getFile(uid: Uid): F[Response] =
        documentationClient.send(HttpMethod.Get, s"/api/files/$uid")

    override def addFile(modelUid: Uid, request: Request): F[Response] = {
        request.uri_=(s"/api/models/$modelUid/files")
        request.method_=(Method.Post)
        equipmentClient.send(request)
    }

    override def removeFile(uid: Uid): F[Response] =
        documentationClient.send(HttpMethod.Delete, "/api/files")
}
