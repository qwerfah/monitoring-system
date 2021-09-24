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

object Startup {
    implicit val defaultEquipmentClient =
        new DefaultHttpClient(Equipment, "localhost:8081")
    implicit val DefaultEquipmentService = new DefaultEquipmentService[Future]
}
