package com.qwerfah.gateway

import scala.concurrent.ExecutionContext.Implicits.global

import com.rms.miu.slickcats.DBIOInstances._
import com.twitter.util.Future
import io.catbird.util._

import slick.jdbc.{PostgresProfile, JdbcProfile}
import slick.jdbc.JdbcBackend.Database
import slick.dbio._

import com.qwerfah.gateway.services.default._
import com.qwerfah.common.http._
import com.qwerfah.common.resources.Credentials

object Startup {
    val defaultSessionClient =
        new DefaultHttpClient(
          Session,
          Credentials("gateway", "gateway"),
          "localhost:8081"
        )
    val defaultEquipmentClient =
        new DefaultHttpClient(
          Equipment,
          Credentials("gateway", "gateway"),
          "localhost:8083"
        )

    implicit val defaultSessionService =
        new DefaultSessionService[Future](defaultSessionClient)
    implicit val defaultEquipmentService =
        new DefaultEquipmentService[Future](defaultEquipmentClient)
}
