package com.qwerfah.generator.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.util.Future
import com.twitter.server.TwitterServer
import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.finagle.http.{Request, Response}

import io.finch._
import io.finch.circe._
import io.finch.catsEffect._

import io.circe.generic.auto._

import io.catbird.util._

import com.qwerfah.generator.Startup
import com.qwerfah.generator.resources._
import com.qwerfah.generator.json.Decoders
import com.qwerfah.generator.services.ParamValueService

import com.qwerfah.common.Uid
import com.qwerfah.common.services.TokenService
import com.qwerfah.common.controllers.Controller

object ParamValueController extends Controller {
    import Startup._
    import Decoders._

    private val paramValueService = implicitly[ParamValueService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    /** Endpoint for route GET params/values */
    private val getAllParamValues = get(
      "params" :: "values" :: headerOption("Authorization")
    ) { header: Option[String] =>
        authorize(header, serviceRoles, _ => paramValueService.get)
    }

    /** Endpoint for route GET params/values/uid */
    private val getParamValue = get(
      "params" :: "values" :: path[Uid] :: headerOption("Authorization")
    ) { (paramUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => paramValueService.get(paramUid)
        )
    }

    /** Endpoint for route GET params/uid/values */
    private val getParamValuesForParam = get(
      "params" :: path[Uid] :: "values" :: headerOption("Authorization")
    ) { (paramUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => paramValueService.get(Some(paramUid), None)
        )
    }

    /** Endpoint for route GET instances/uid/params/values */
    private val getParamValuesForInsatance = get(
      "instances" :: path[Uid] :: "params" :: "values" :: headerOption(
        "Authorization"
      )
    ) { (instanceUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => paramValueService.get(None, Some(instanceUid))
        )
    }

    /** Endpoint for route GET instances/uid/params/uid/values */
    private val getParamValuesForInsatanceParam = get(
      "instances" :: path[Uid] :: "params" :: path[
        Uid
      ] :: "values" :: headerOption(
        "Authorization"
      )
    ) { (instanceUid: Uid, paramUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => paramValueService.get(Some(paramUid), Some(instanceUid))
        )
    }

    /** Endpoint for route GET instances/uid/params/uid/values/last */
    private val getLastParamValueForInsatanceParam = get(
      "instances" :: path[Uid] :: "params" :: path[
        Uid
      ] :: "values" :: "last" :: headerOption(
        "Authorization"
      )
    ) { (instanceUid: Uid, paramUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => paramValueService.getLast(paramUid, instanceUid)
        )
    }

    /** Endpoint for route POST params/values */
    private val addParamValue = post(
      "params" :: "values" :: jsonBody[ParamValueRequest] :: headerOption(
        "Authorization"
      )
    ) { (request: ParamValueRequest, header: Option[String]) =>
        authorize(header, serviceRoles, _ => paramValueService.add(request))
    }

    /** Endpoint for route DELETE params/values/uid */
    private val removeParamValue = delete(
      "params" :: "values" :: path[Uid] :: headerOption(
        "Authorization"
      )
    ) { (paramUid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => paramValueService.remove(paramUid))
    }

    /** Endpoint for route DELETE params/uid/values */
    private val removeParamValuesForParam = delete(
      "params" :: path[Uid] :: "values" :: headerOption(
        "Authorization"
      )
    ) { (paramUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => paramValueService.removeByParamUid(paramUid)
        )
    }

    /** Endpoint for route DELETE params/uid/values */
    private val removeParamValuesForInstance = delete(
      "instances" :: path[Uid] :: "params" :: "values" :: headerOption(
        "Authorization"
      )
    ) { (instanceUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => paramValueService.removeByInstanceUid(instanceUid)
        )
    }

    val api = "api" :: getAllParamValues
        .:+:(getParamValue)
        .:+:(getParamValuesForParam)
        .:+:(getParamValuesForInsatance)
        .:+:(getParamValuesForInsatanceParam)
        .:+:(getLastParamValueForInsatanceParam)
        .:+:(addParamValue)
        .:+:(removeParamValue)
        .:+:(removeParamValuesForParam)
        .:+:(removeParamValuesForInstance)
        .handle(errorHandler)
}
