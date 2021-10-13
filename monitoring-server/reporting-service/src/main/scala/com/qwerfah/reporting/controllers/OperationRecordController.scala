package com.qwerfah.reporting.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import java.time.LocalDateTime

import com.twitter.util.Future
import com.twitter.server.TwitterServer
import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.finagle.http.{Request, Response}

import io.finch._
import io.finch.circe._
import io.finch.catsEffect._

import io.circe.generic.auto._

import io.catbird.util._

import com.qwerfah.reporting.Startup
import com.qwerfah.reporting.json.Decoders
import com.qwerfah.reporting.models.ReportingContext
import com.qwerfah.reporting.services.OperationRecordService

import com.qwerfah.common.Uid
import com.qwerfah.common.http.HttpMethod
import com.qwerfah.common.services.TokenService
import com.qwerfah.common.controllers.Controller
import com.qwerfah.common.resources.{RecordRequest, RecordResponse}

object OperationRecordController extends Controller {
    import Startup._
    import Decoders._

    implicit val dateTimeDecoder: DecodeEntity[LocalDateTime] =
        DecodeEntity.instance(s => Right(LocalDateTime.parse(s)))

    implicit val methodDecoder: DecodeEntity[HttpMethod] =
        DecodeEntity.instance(s => Right(HttpMethod.withName(s)))

    private val recordService = implicitly[OperationRecordService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    private val getRecords = get(
      "records" :: headerOption[String]("Authorization")
    ) { header: Option[String] =>
        authorize(header, serviceRoles, _ => recordService.get)
    }

    private val getRecord = get(
      "records" :: path[Uid] :: headerOption[String](
        "Authorization"
      )
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => recordService.get(uid))
    }

    private val getRecordsByParams = get(
      "records" :: paramOption[String]("serviceId") :: paramOption[
        HttpMethod
      ]("method") :: paramOption[
        Int
      ]("status") :: paramOption[LocalDateTime]("fromDate") :: paramOption[
        LocalDateTime
      ]("toDate") :: headerOption[String](
        "Authorization"
      )
    ) {
        (
          serviceId: Option[String],
          method: Option[HttpMethod],
          status: Option[Int],
          fromDate: Option[LocalDateTime],
          toDate: Option[LocalDateTime],
          header: Option[String]
        ) =>
            authorize(
              header,
              serviceRoles,
              _ =>
                  recordService.get(
                    serviceId,
                    method,
                    status,
                    fromDate,
                    toDate
                  )
            )
    }

    private val removeRecord = delete(
      "records" :: path[Uid] :: headerOption[String](
        "Authorization"
      )
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => recordService.remove(uid))
    }

    private val removeServiceRecords = delete(
      "services" :: path[String] :: "records" :: headerOption[String](
        "Authorization"
      )
    ) { (serviceId: String, header: Option[String]) =>
        authorize(header, serviceRoles, _ => recordService.remove(serviceId))
    }

    private val restoreRecord = patch(
      "records" :: path[Uid] :: "restore" :: headerOption[String](
        "Authorization"
      )
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => recordService.restore(uid))
    }

    private val restoreServiceRecords = patch(
      "services" :: path[String] :: "records" :: "restore" :: headerOption[
        String
      ](
        "Authorization"
      )
    ) { (serviceId: String, header: Option[String]) =>
        authorize(header, serviceRoles, _ => recordService.restore(serviceId))
    }

    val api = "api" :: "reports" :: getRecords
        .:+:(getRecord)
        .:+:(getRecordsByParams)
        .:+:(removeRecord)
        .:+:(removeServiceRecords)
        .:+:(restoreRecord)
        .:+:(restoreServiceRecords)
        .handle(errorHandler)
}
