package com.qwerfah.equipment

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext, Future}

import slick.jdbc.{PostgresProfile, JdbcProfile}
import slick.jdbc.JdbcBackend.Database
import slick.dbio._

import com.qwerfah.equipment.models._
import com.qwerfah.equipment.repos.slick._
import com.qwerfah.equipment.repos._
import com.qwerfah.equipment.services._
import com.qwerfah.equipment.services.default._

import com.rms.miu.slickcats.DBIOInstances._

/** Contain implicitly defined dependencies for db profile, db instance, data
  * context and all repositories and instances.
  */
object Startup {
    // Db dependencies
    implicit val pgdb = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context = new DataContext

    // Repository dependencies
    implicit val modelRepo = new SlickEquipmentModelRepo
    implicit val instanceRepo = new SlickEquipmentInstanceRepo
    implicit val paramRepo = new SlickParamRepo
    implicit val dbManager = new SlickDbManager

    // Service dependencies
    implicit val defaultModelService =
        new DefaultEquipmentModelService[Future, DBIO]
    implicit val defaultInstanceService =
        new DefaultEquipmentInstanceService[Future, DBIO]
    implicit val defaultParamService =
        new DefaultParamService[Future, DBIO]

    def startup() =
        Await.result(dbManager.execute(context.setup), Duration.Inf)
}
