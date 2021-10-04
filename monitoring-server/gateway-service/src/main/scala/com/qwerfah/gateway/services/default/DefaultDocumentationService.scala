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
  documentationClient: HttpClient[F],
  equipmentClient: HttpClient[F]
) extends DocumentationService[F] {

    override def getFiles: F[Response] =
        documentationClient.send(HttpMethod.Get, "/api/files")

    override def getModelFiles(modelUid: Uid): F[Response] =
        equipmentClient.send(HttpMethod.Get, s"/api/models/$modelUid") flatMap {
            response =>
                response.status match {
                    case Status.Ok =>
                        documentationClient.send(
                          HttpMethod.Get,
                          s"/api/models/$modelUid/files"
                        )
                    case _ => Monad[F].pure(response)
                }
        }

    override def getFile(uid: Uid): F[Response] =
        documentationClient.send(HttpMethod.Get, s"/api/files/$uid")

    override def addFile(modelUid: Uid, request: Request): F[Response] = {
        equipmentClient.send(
          HttpMethod.Get,
          s"/api/models/$modelUid"
        ) flatMap { response =>
            response.status match {
                case Status.Ok => {
                    request.uri_=(s"/api/models/$modelUid/files")
                    request.method_=(Method.Post)
                    documentationClient.send(request)
                }
                case _ => Monad[F].pure(response)
            }
        }
    }

    override def removeFile(uid: Uid): F[Response] =
        documentationClient.send(HttpMethod.Delete, "/api/files")

    override def removeModelFiles(modelUid: Uid): F[Response] =
        equipmentClient.send(
          HttpMethod.Get,
          s"/api/models/$modelUid"
        ) flatMap { response =>
            response.status match {
                case Status.Ok =>
                    documentationClient.send(
                      HttpMethod.Delete,
                      s"/api/models/$modelUid/files"
                    )
                case _ => Monad[F].pure(response)
            }
        }

}
