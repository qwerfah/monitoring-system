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

import com.qwerfah.generator.models.GeneratorContext
import com.qwerfah.generator.repos.slick.SlickParamValueRepo
import com.qwerfah.generator.services.default.DefaultParamValueService

import com.qwerfah.common.http.Equipment
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.http.DefaultHttpClient
import com.qwerfah.common.db.slick.SlickDbManager

object Startup {
    implicit val config = ConfigFactory.load

    implicit val pgdb = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context = new GeneratorContext
    implicit val dbManager = new SlickDbManager

    val defaultEquipmentClient =
        new DefaultHttpClient(
          Equipment,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("equipmentUrl")
        )

    implicit val paramValueRepo = new SlickParamValueRepo

    implicit val defaultParamValueService =
        new DefaultParamValueService[Future, DBIO](defaultEquipmentClient)

    def startup() = Await.result(dbManager.execute(context.setup))
}
