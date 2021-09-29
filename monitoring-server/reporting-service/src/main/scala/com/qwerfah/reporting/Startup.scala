package com.qwerfah.reporting

import scala.concurrent.ExecutionContext.Implicits.global

import io.catbird.util._
import com.twitter.util.{Future, Await}
import com.rms.miu.slickcats.DBIOInstances._

import slick.dbio._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.{PostgresProfile, JdbcProfile}

import com.typesafe.config.ConfigFactory

import com.qwerfah.reporting.repos.slick._
import com.qwerfah.reporting.services.default._
import com.qwerfah.reporting.models.ReportingContext
import com.qwerfah.reporting.repos.OperationRecordRepo

import com.qwerfah.common.http._
import com.qwerfah.common.services.default._
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.db.slick.SlickDbManager
import com.qwerfah.common.repos.slick.SlickUserRepo
import com.qwerfah.common.repos.local.LocalTokenRepo

object Startup {
    implicit val config = ConfigFactory.load

    implicit val pgdb = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context = new ReportingContext

    implicit val monitorRepo = new SlickOperationRecordRepo
    implicit val tokenRepo = new LocalTokenRepo
    implicit val userRepo = new SlickUserRepo
    implicit val dbManager = new SlickDbManager

    implicit val DefaultEquipmentService =
        new DefaultOperationRecordService[Future, DBIO]
    implicit val defaultTokenService = new DefaultTokenService[Future, DBIO]
    implicit val defaultUserService = new DefaultUserService[Future, DBIO]

    def startup() = Await.result(dbManager.execute(context.setup))
}
