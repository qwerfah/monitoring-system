package com.qwerfah.documentation

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

import com.qwerfah.documentation.repos.slick.SlickFileRepo
import com.qwerfah.documentation.models.DocumentationContext
import com.qwerfah.documentation.services.default.DefaultFileService

import com.qwerfah.common.services.default._
import com.qwerfah.common.db.slick.SlickDbManager
import com.qwerfah.common.repos.slick.SlickUserRepo
import com.qwerfah.common.repos.local.LocalTokenRepo
import com.qwerfah.common.controllers.RequestReportingFilter

object Startup {
    // Application config
    implicit val config = ConfigFactory.load

    // Db dependencies
    implicit val pgdb = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context = new DocumentationContext
    implicit val dbManager = new SlickDbManager

    // Repositories
    implicit val tokenRepo = new LocalTokenRepo
    implicit val userRepo = new SlickUserRepo
    implicit val modelRepo = new SlickFileRepo

    implicit val defaultTokenService = new DefaultTokenService[Future, DBIO]
    implicit val defaultFileService = new DefaultFileService[Future, DBIO]

    implicit val actorSystem = ActorSystem("such-system")
    implicit val rabbitControl = actorSystem.actorOf(Props[RabbitControl]())
    implicit val recoveryStrategy = RecoveryStrategy.none

    object RequestReportingFilter
      extends RequestReportingFilter[Request, Future]

    def startup() = Await.result(dbManager.execute(context.setup))
}
