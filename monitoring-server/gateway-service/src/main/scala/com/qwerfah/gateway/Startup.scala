package com.qwerfah.gateway

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext, Future}

import slick.jdbc.{PostgresProfile, JdbcProfile}
import slick.jdbc.JdbcBackend.Database
import slick.dbio._

import com.qwerfah.gateway.services.default._

import com.rms.miu.slickcats.DBIOInstances._

object Startup {
    implicit val DefaultEquipmentService = new DefaultEquipmentService
}