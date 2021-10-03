package com.qwerfah.documentation.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.io.{Reader, Buf}
import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.{Future, FuturePool}
import com.twitter.finagle.http.exp.Multipart
import com.twitter.finagle.http.exp.Multipart._
import com.twitter.io.{Reader, Buf}

import io.finch._
import io.finch.circe._
import io.finch.catsEffect._

import io.circe.generic.auto._

import io.catbird.util._

import com.qwerfah.documentation.Startup
import com.qwerfah.documentation.services.FileService
import com.qwerfah.documentation.resources.FileRequest

import com.qwerfah.common.Uid
import com.qwerfah.common.services.TokenService
import com.qwerfah.common.controllers.Controller
import com.qwerfah.common.exceptions.NoMultipartData
import com.qwerfah.common.services.response.OkResponse

object FileController extends Controller {
    import Startup._

    private val fileService = implicitly[FileService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    private val getFiles =
        get("files" :: headerOption("Authorization")) {
            header: Option[String] =>
                authorize(header, serviceRoles, _ => fileService.getMeta)
        }

    private val getModelFiles =
        get("models" :: path[Uid] :: "files" :: headerOption("Authorization")) {
            (modelUid: Uid, header: Option[String]) =>
                authorize(
                  header,
                  serviceRoles,
                  _ => fileService.getModelMeta(modelUid)
                )
        }

    private val getFile =
        get("files" :: path[Uid] :: headerOption("Authorization")) {
            (uid: Uid, header: Option[String]) =>
                authorizeNoWrap(
                  header,
                  serviceRoles,
                  _ => fileService.get(uid)
                ) map {
                    case OkResponse(file) =>
                        Ok(Buf.ByteArray(file.content.toIndexedSeq: _*))
                            .withHeader("Content-Type" -> file.contentType)
                    case other => other.asOutputError
                }
        }

    private val addFile =
        post(
          "models" :: path[Uid] :: "files" :: headerOption(
            "Authorization"
          ) :: multipartFileUploadOption("file")
        ) { (modelUid: Uid, header: Option[String], data: Option[FileUpload]) =>
            data match {
                case Some(file) => {
                    val reader = file match {
                        case d: Multipart.OnDiskFileUpload => {
                            Reader.fromFile(d.content)
                        }
                        case m: Multipart.InMemoryFileUpload =>
                            Reader.fromBuf(m.content)
                    }

                    Reader.readAllItems(reader) flatMap { buf =>
                        val content = Buf.ByteArray.Owned.extract(Buf(buf))
                        val request = FileRequest(
                          modelUid,
                          file.fileName,
                          file.contentType,
                          content
                        )
                        authorize(
                          header,
                          serviceRoles,
                          _ => fileService.add(request)
                        )
                    }
                }
                case None =>
                    FuturePool immediatePool { BadRequest(NoMultipartData) }
            }
        }

    private val removeFile =
        delete("files" :: path[Uid] :: headerOption("Authorization")) {
            (uid: Uid, header: Option[String]) =>
                authorize(header, serviceRoles, _ => fileService.remove(uid))
        }

    private val removeModelFiles =
        delete(
          "models" :: path[Uid] :: "files" :: headerOption("Authorization")
        ) { (modelUid: Uid, header: Option[String]) =>
            authorize(
              header,
              serviceRoles,
              _ => fileService.removeModelFiles(modelUid)
            )
        }

    val api = "api" :: getFiles
        .:+:(getModelFiles)
        .:+:(getFile)
        .:+:(addFile)
        .:+:(removeFile)
        .:+:(removeModelFiles)
        .handle(errorHandler)
}
