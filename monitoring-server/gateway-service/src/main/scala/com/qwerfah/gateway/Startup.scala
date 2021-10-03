package com.qwerfah.gateway

import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.config.ConfigFactory

import com.rms.miu.slickcats.DBIOInstances._

import com.twitter.util.Future
import com.twitter.finagle.http.Request

import akka.actor.{ActorSystem, Props}

import com.spingo.op_rabbit._
import com.spingo.op_rabbit.CirceSupport._

import io.catbird.util._

import slick.jdbc.{PostgresProfile, JdbcProfile}
import slick.jdbc.JdbcBackend.Database
import slick.dbio._

import com.qwerfah.gateway.services.default._
import com.qwerfah.common.http._
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.controllers.RequestReportingFilter

object Startup {
    implicit val config = ConfigFactory.load

    val sessionClient = new DefaultHttpClient(
      ServiceTag.Session,
      Credentials(
        config.getString("serviceId"),
        config.getString("secret")
      ),
      config.getString("sessionUrl")
    )
    val equipmentClient = new DefaultHttpClient(
      ServiceTag.Equipment,
      Credentials(
        config.getString("serviceId"),
        config.getString("secret")
      ),
      config.getString("equipmentUrl")
    )
    val documentationClient = new DefaultHttpClient(
      ServiceTag.Documentation,
      Credentials(
        config.getString("serviceId"),
        config.getString("secret")
      ),
      config.getString("documentationUrl")
    )
    val monitoringClient = new DefaultHttpClient(
      ServiceTag.Monitoring,
      Credentials(
        config.getString("serviceId"),
        config.getString("secret")
      ),
      config.getString("monitoringUrl")
    )
    val generatorClient = new DefaultHttpClient(
      ServiceTag.Generator,
      Credentials(
        config.getString("serviceId"),
        config.getString("secret")
      ),
      config.getString("generatorUrl")
    )
    val reportingClient = new DefaultHttpClient(
      ServiceTag.Reporting,
      Credentials(
        config.getString("serviceId"),
        config.getString("secret")
      ),
      config.getString("reportingUrl")
    )

    implicit val defaultSessionService =
        new DefaultSessionService[Future](sessionClient)
    implicit val defaultEquipmentService =
        new DefaultEquipmentService[Future](equipmentClient)
    implicit val defaultDocumentationService =
        new DefaultDocumentationService[Future](
          documentationClient,
          equipmentClient
        )
    implicit val defaultMonitoringService =
        new DefaultMonitoringService[Future](monitoringClient)

    implicit val actorSystem = ActorSystem("such-system")
    implicit val rabbitControl = actorSystem.actorOf(Props[RabbitControl]())
    implicit val recoveryStrategy = RecoveryStrategy.none

    object RequestReportingFilter
      extends RequestReportingFilter[Request, Future]
}
