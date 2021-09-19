package com.qwerfah.equipment.controllers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import io.finch.catsEffect._
import io.finch._
import io.finch.circe._

import io.circe.generic.auto._

import com.qwerfah.equipment.services._
import com.qwerfah.equipment.repos.slick._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.Startup
import com.qwerfah.equipment.json.Decoders
import com.qwerfah.common.Uid
import com.qwerfah.common.services._
import com.qwerfah.common.exceptions._

object ParamController {
    import Startup._
    import Decoders._

    private val paramService = implicitly[ParamService[Future]]

    private val getParams = get("params") {
        for { result <- paramService.get } yield result match {
            case ObjectResponse(params) => Ok(params)
        }
    } handle { case e: Exception =>
        InternalServerError(e)
    }

    private val getParam = get("params" :: path[Uid]) { uid: Uid =>
        for { result <- paramService.getByUid(uid) } yield result match {
            case ObjectResponse(param) => Ok(param)
            case EmptyResponse         => NotFound(NoParamException(uid))
        }
    } handle { case e: Exception =>
        InternalServerError(e)
    }

    private val addParam =
        post("params" :: jsonBody[AddParamRequest]) {
            request: AddParamRequest =>
                for {
                    result <- paramService.add(request)
                } yield result match {
                    case ObjectResponse(param) => Ok(param)
                    case EmptyResponse =>
                        NotFound(NoModelException(request.modelUid))
                }
        } handle {
            case e: InvalidJsonBodyException => BadRequest(e)
            case e: Exception =>
                InternalServerError(e)
        }

    private val updateParam =
        patch("params" :: path[Uid] :: jsonBody[UpdateParamRequest]) {
            (uid: Uid, request: UpdateParamRequest) =>
                for {
                    result <- paramService.update(uid, request)
                } yield result match {
                    case response: StringResponse => Ok(response)
                    case EmptyResponse =>
                        NotFound(NoParamException(uid))
                }
        } handle {
            case e: InvalidJsonBodyException => BadRequest(e)
            case e: Exception =>
                InternalServerError(e)
        }

    private val deleteParam = delete("params" :: path[Uid]) { uid: Uid =>
        for {
            result <- paramService.removeByUid(uid)
        } yield result match {
            case response: StringResponse => Ok(response)
            case EmptyResponse =>
                NotFound(NoParamException(uid))
        }
    } handle { case e: Exception =>
        InternalServerError(e)
    }

    val api =
        getParams :+: getParam :+: addParam :+: updateParam :+: deleteParam
}
