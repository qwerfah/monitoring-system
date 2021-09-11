package main

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext, Future}

import slick.jdbc.{PostgresProfile, JdbcProfile}
import slick.jdbc.JdbcBackend.Database
import slick.dbio._

import models._
import repos.slick._
import repos._
import services.default._

import com.rms.miu.slickcats.DBIOInstances._

/** Contain implicitly defined dependencies for db profile, db instance, data
  * context and all repositories and instances.
  */
object Startup {
    // Db dependencies
    implicit val pgdb: Database = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context: DataContext = new DataContext

    // Repository dependencies
    implicit val modelRepo = new SlickEquipmentModelRepo
    implicit val instanceRepo = new SlickEquipmentInstanceRepo
    implicit val paramRepo = new SlickParamRepo
    implicit val dbManager = new SlickDbManager

    // Service dependencies
    implicit val modelService =
        new DefaultEquipmentModelService[Future, DBIO]

    def startup() =
        Await.result(dbManager.execute(context.setup), Duration.Inf)
}
