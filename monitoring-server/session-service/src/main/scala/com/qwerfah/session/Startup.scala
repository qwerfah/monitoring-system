package com.qwerfah.session

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

import slick.jdbc.{PostgresProfile, JdbcProfile}
import slick.jdbc.JdbcBackend.Database
import slick.dbio._

import com.rms.miu.slickcats.DBIOInstances._

import com.qwerfah.session.models._
import com.qwerfah.common.repos.slick._
import com.qwerfah.common.repos._
import com.qwerfah.common.services._
import com.qwerfah.common.services.default._
import com.qwerfah.common.repos.local._
import com.qwerfah.common.db.slick.SlickDbManager
import com.qwerfah.common.services.default._

object Startup {
    // Db dependencies
    implicit val pgdb = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context = new SessionContext

    // Repository dependencies
    implicit val userRepo = new SlickUserRepo
    implicit val tokenRepo = new LocalTokenRepo
    implicit val dbManager = new SlickDbManager

    // Service dependencies
    implicit val DefaultTokenService = new DefaultTokenService[Future, DBIO]
    implicit val defaultUserService = new DefaultUserService[Future, DBIO]

    def startup() =
        Await.result(dbManager.execute(context.setup), Duration.Inf)
}
