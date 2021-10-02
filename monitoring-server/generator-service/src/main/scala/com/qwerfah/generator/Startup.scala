package com.qwerfah.generator

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

import com.qwerfah.generator.services.default._
import com.qwerfah.generator.models.GeneratorContext
import com.qwerfah.generator.repos.slick.SlickParamValueRepo

import com.qwerfah.common.http.ServiceTag
import com.qwerfah.common.services.default._
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.http.DefaultHttpClient
import com.qwerfah.common.db.slick.SlickDbManager
import com.qwerfah.common.repos.slick.SlickUserRepo
import com.qwerfah.common.repos.local.LocalTokenRepo
import com.twitter.util.JavaTimer
import com.twitter.util.Duration

object Startup {
    implicit val config = ConfigFactory.load

    implicit val pgdb = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context = new GeneratorContext
    implicit val dbManager = new SlickDbManager

    val equipmentClient =
        new DefaultHttpClient(
          ServiceTag.Generator,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("equipmentUrl")
        )

    val monitoringClient =
        new DefaultHttpClient(
          ServiceTag.Generator,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("monitoringUrl")
        )

    implicit val paramValueRepo = new SlickParamValueRepo
    implicit val tokenRepo = new LocalTokenRepo
    implicit val userRepo = new SlickUserRepo

    implicit val defaultParamValueService =
        new DefaultParamValueService[Future, DBIO](equipmentClient)
    implicit val defaultGeneratorService =
        new DefaultGeneratorService[Future, DBIO](
          monitoringClient,
          equipmentClient
        )
    implicit val defaultTokenService = new DefaultTokenService[Future, DBIO]
    implicit val defaultUserService = new DefaultUserService[Future, DBIO]

    def startup() = Await.result(dbManager.execute(context.setup))

    implicit val actorSystem = ActorSystem("such-system")
    val rabbitControl = actorSystem.actorOf(Props[RabbitControl]())
    implicit val recoveryStrategy = RecoveryStrategy.none

    val timer = new JavaTimer
    timer.schedule(Duration.fromSeconds(config.getInt("genPeriod"))) { defaultGeneratorService.generate }

}
