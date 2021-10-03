package com.qwerfah.gateway.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.util.Future
import com.twitter.server.TwitterServer
import com.twitter.finagle.{SimpleFilter, Service}
import com.twitter.finagle.http.exp.Multipart.FileUpload
import com.twitter.finagle.http.{Request, Response, Status}

import io.finch.catsEffect._
import io.finch._
import io.finch.circe._

import io.circe.generic.auto._

import io.catbird.util._

import com.qwerfah.common.Uid
import com.qwerfah.gateway.Startup
import com.qwerfah.common.exceptions._
import com.qwerfah.documentation.json.Decoders
import com.qwerfah.common.services.TokenService
import com.qwerfah.common.controllers.Controller
import com.qwerfah.gateway.services.DocumentationService

object DocumentationController extends Controller {
    import Startup._
    import Decoders._

    private val fileService = implicitly[DocumentationService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    private val getFiles = get("files" :: headerOption("Authorization")) {
        header: Option[String] =>
            authorizeRaw(header, readRoles, _ => fileService.getFiles)
    }

    private val getModelFiles =
        get("models" :: path[Uid] :: "files" :: headerOption("Authorization")) {
            (modelUid: Uid, header: Option[String]) =>
                authorizeRaw(
                  header,
                  serviceRoles,
                  _ => fileService.getModelFiles(modelUid)
                )
        }

    private val getFile = get(
      "files" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorizeRaw(
          header,
          serviceRoles,
          _ => fileService.getFile(uid)
        )
    }

    private val addFile =
        post(
          root ::
              "models" :: path[Uid] :: "files" :: headerOption(
                "Authorization"
              ) :: multipartFileUploadOption("file")
        ) {
            (
              request: Request,
              modelUid: Uid,
              header: Option[String],
              _: Option[FileUpload]
            ) =>
                authorizeRaw(
                  header,
                  serviceRoles,
                  _ => fileService.addFile(modelUid, request)
                )
        }

    private val removeFile =
        delete("files" :: path[Uid] :: headerOption("Authorization")) {
            (uid: Uid, header: Option[String]) =>
                authorizeRaw(
                  header,
                  serviceRoles,
                  _ => fileService.removeFile(uid)
                )
        }

    val api = ("api" :: "documentation") :: (getFiles
        .:+:(getFile.:+:(getModelFiles.:+:(addFile.:+:(removeFile)))))
        .handle(errorHandler)
}
