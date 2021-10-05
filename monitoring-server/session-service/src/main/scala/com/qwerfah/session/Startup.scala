package com.qwerfah.session

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.util.{Future, Await}
import com.twitter.finagle.http.Request

import slick.jdbc.{PostgresProfile, JdbcProfile}
import slick.jdbc.JdbcBackend.Database
import slick.dbio._

import akka.actor.{ActorSystem, Props}

import com.spingo.op_rabbit._
import com.spingo.op_rabbit.CirceSupport._

import io.catbird.util._

import com.rms.miu.slickcats.DBIOInstances._

import com.typesafe.config.ConfigFactory

import com.qwerfah.session.models._
import com.qwerfah.session.services.default.DefaultUserService

import com.qwerfah.common.repos.slick._
import com.qwerfah.common.repos.local._
import com.qwerfah.common.services.default._
import com.qwerfah.common.services.default._
import com.qwerfah.common.db.slick.SlickDbManager
import com.qwerfah.common.controllers.RequestReportingFilter

object Startup {
    implicit val config = ConfigFactory.load

    // Db dependencies
    implicit val pgdb = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context = new SessionContext

    // Repository dependencies
    implicit val userRepo = new SlickUserRepo
    implicit val tokenRepo = new LocalTokenRepo
    implicit val dbManager = new SlickDbManager

    // Service dependencies
    implicit val userTokenService = new DefaultTokenService[Future, DBIO]
    implicit val externalUserService = new DefaultUserService[Future, DBIO]

    implicit val actorSystem = ActorSystem("such-system")
    implicit val rabbitControl = actorSystem.actorOf(Props[RabbitControl]())
    implicit val recoveryStrategy = RecoveryStrategy.none

    object RequestReportingFilter
      extends RequestReportingFilter[Request, Future]

    def startup() = Await.result(dbManager.execute(context.setup))
}
