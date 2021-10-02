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

    val defaultSessionClient =
        new DefaultHttpClient(
          ServiceTag.Gateway,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("sessionUrl")
        )
    val defaultEquipmentClient =
        new DefaultHttpClient(
          ServiceTag.Gateway,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("equipmentUrl")
        )
    val defaultDocumentationClient =
        new DefaultHttpClient(
          ServiceTag.Gateway,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("documentationUrl")
        )
    val defaultMonitoringClient =
        new DefaultHttpClient(
          ServiceTag.Gateway,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("monitoringUrl")
        )
    val defaultGeneratorClient =
        new DefaultHttpClient(
          ServiceTag.Gateway,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("generatorUrl")
        )
    val defaultReportingClient =
        new DefaultHttpClient(
          ServiceTag.Gateway,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("reportingUrl")
        )

    implicit val defaultSessionService =
        new DefaultSessionService[Future](defaultSessionClient)
    implicit val defaultEquipmentService =
        new DefaultEquipmentService[Future](defaultEquipmentClient)

    implicit val actorSystem = ActorSystem("such-system")
    implicit val rabbitControl = actorSystem.actorOf(Props[RabbitControl]())
    implicit val recoveryStrategy = RecoveryStrategy.none

    object RequestReportingFilter
      extends RequestReportingFilter[Request, Future]
}
