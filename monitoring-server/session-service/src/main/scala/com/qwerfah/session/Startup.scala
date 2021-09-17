package com.qwerfah.session

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext, Future}

import slick.jdbc.{PostgresProfile, JdbcProfile}
import slick.jdbc.JdbcBackend.Database
import slick.dbio._

import com.qwerfah.session.models._
import com.qwerfah.session.repos.slick._
import com.qwerfah.session.repos._
import com.qwerfah.common.db.slick.SlickDbManager
import com.qwerfah.session.services._
import com.qwerfah.session.services.default._

import com.rms.miu.slickcats.DBIOInstances._

object Startup {
    // Db dependencies
    implicit val pgdb = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context = new DataContext

    // Repository dependencies
    implicit val userRepo = new SlickUserRepo
    implicit val dbManager = new SlickDbManager

    // Service dependencies
    implicit val defaultUserService =
        new DefaultUserService[Future, DBIO]

    def startup() =
        Await.result(dbManager.execute(context.setup), Duration.Inf)
}
