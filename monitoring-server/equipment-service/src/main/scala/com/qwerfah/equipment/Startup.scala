package com.qwerfah.equipment

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import slick.jdbc.{PostgresProfile, JdbcProfile}
import slick.jdbc.JdbcBackend.Database
import slick.dbio._

import com.twitter.util.{Future, Await}
import com.twitter.finagle.http.Request

import akka.actor.{ActorSystem, Props}

import com.spingo.op_rabbit._
import com.spingo.op_rabbit.CirceSupport._

import io.catbird.util._

import com.rms.miu.slickcats.DBIOInstances._

import com.typesafe.config.ConfigFactory

import com.qwerfah.equipment.models._
import com.qwerfah.equipment.repos.slick._
import com.qwerfah.equipment.repos._
import com.qwerfah.equipment.services._
import com.qwerfah.equipment.services.default._

import com.qwerfah.common.db.slick.SlickDbManager
import com.qwerfah.common.repos.local.LocalTokenRepo
import com.qwerfah.common.repos.slick.SlickUserRepo
import com.qwerfah.common.services.default._
import com.qwerfah.common.controllers.RequestReportingFilter
import com.qwerfah.common.http.{DefaultHttpClient, ServiceTag}
import com.qwerfah.common.resources.Credentials

/** Contain implicitly defined dependencies for db profile, db instance, data
  * context and all repositories and instances.
  */
object Startup {
    // Application config
    implicit val config = ConfigFactory.load

    val monitoringClient = new DefaultHttpClient(
      ServiceTag.Monitoring,
      Credentials(
        config.getString("serviceId"),
        config.getString("secret")
      ),
      config.getString("monitoringUrl")
    )

    val generatorClient = new DefaultHttpClient(
      ServiceTag.Monitoring,
      Credentials(
        config.getString("serviceId"),
        config.getString("secret")
      ),
      config.getString("generatorUrl")
    )

    // Db dependencies
    implicit val pgdb = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context = new EquipmentContext

    // Repository dependencies
    implicit val modelRepo = new SlickEquipmentModelRepo
    implicit val instanceRepo = new SlickEquipmentInstanceRepo
    implicit val paramRepo = new SlickParamRepo
    implicit val tokenRepo = new LocalTokenRepo
    implicit val userRepo = new SlickUserRepo
    implicit val dbManager = new SlickDbManager

    // Service dependencies
    implicit val defaultModelService =
        new DefaultEquipmentModelService[Future, DBIO]
    implicit val defaultInstanceService =
        new DefaultEquipmentInstanceService[Future, DBIO](
          monitoringClient,
          generatorClient
        )
    implicit val defaultParamService = new DefaultParamService[Future, DBIO]
    implicit val defaultTokenService = new DefaultTokenService[Future, DBIO]

    implicit val actorSystem = ActorSystem("such-system")
    implicit val rabbitControl = actorSystem.actorOf(Props[RabbitControl]())
    implicit val recoveryStrategy = RecoveryStrategy.none

    object RequestReportingFilter
      extends RequestReportingFilter[Request, Future]

    def startup() = Await.result(dbManager.execute(context.setup))
}
