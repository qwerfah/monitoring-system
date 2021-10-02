package com.qwerfah.monitoring

import scala.concurrent.ExecutionContext.Implicits.global

import com.rms.miu.slickcats.DBIOInstances._

import com.twitter.util.{Future, Await}
import com.twitter.finagle.http.Request

import io.catbird.util._

import slick.jdbc.{PostgresProfile, JdbcProfile}
import slick.jdbc.JdbcBackend.Database
import slick.dbio._

import akka.actor.{ActorSystem, Props}

import com.spingo.op_rabbit._
import com.spingo.op_rabbit.CirceSupport._

import com.typesafe.config.ConfigFactory

import com.qwerfah.monitoring.repos.slick._
import com.qwerfah.monitoring.repos.MonitorRepo
import com.qwerfah.monitoring.services.default._
import com.qwerfah.monitoring.models.MonitoringContext

import com.qwerfah.common.http._
import com.qwerfah.common.services.default._
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.db.slick.SlickDbManager
import com.qwerfah.common.repos.slick.SlickUserRepo
import com.qwerfah.common.repos.local.LocalTokenRepo
import com.qwerfah.common.controllers.RequestReportingFilter

object Startup {
    implicit val config = ConfigFactory.load

    implicit val pgdb = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context = new MonitoringContext

    implicit val monitorRepo = new SlickMonitorRepo
    implicit val monitorParamRepo = new SlickMonitorParamRepo
    implicit val tokenRepo = new LocalTokenRepo
    implicit val userRepo = new SlickUserRepo
    implicit val dbManager = new SlickDbManager

    val defaultEquipmentClient =
        new DefaultHttpClient(
          ServiceTag.Monitoring,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("equipmentUrl")
        )

    implicit val DefaultEquipmentService =
        new DefaultMonitorService[Future, DBIO](defaultEquipmentClient)
    implicit val defaultTokenService = new DefaultTokenService[Future, DBIO]
    implicit val defaultUserService = new DefaultUserService[Future, DBIO]

    implicit val actorSystem = ActorSystem("such-system")
    implicit val rabbitControl = actorSystem.actorOf(Props[RabbitControl]())
    implicit val recoveryStrategy = RecoveryStrategy.none

    object RequestReportingFilter
      extends RequestReportingFilter[Request, Future]

    def startup() = Await.result(dbManager.execute(context.setup))
}
