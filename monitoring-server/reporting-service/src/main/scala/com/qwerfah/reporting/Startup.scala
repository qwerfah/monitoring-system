package com.qwerfah.reporting

import scala.concurrent.ExecutionContext.Implicits.global

import io.catbird.util._
import com.twitter.util.{Future, Await}
import com.rms.miu.slickcats.DBIOInstances._

import slick.dbio._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.{PostgresProfile, JdbcProfile}

import com.typesafe.config.ConfigFactory

import akka.actor.{ActorSystem, Props}

import com.spingo.op_rabbit._
import com.spingo.op_rabbit.CirceSupport._

import io.circe._
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.auto._

import com.qwerfah.reporting.repos.slick._
import com.qwerfah.reporting.services.default._
import com.qwerfah.reporting.models.ReportingContext
import com.qwerfah.reporting.repos.OperationRecordRepo

import com.qwerfah.common.http._
import com.qwerfah.common.randomUid
import com.qwerfah.common.services.default._
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.db.slick.SlickDbManager
import com.qwerfah.common.resources.RecordRequest
import com.qwerfah.common.repos.slick.SlickUserRepo
import com.qwerfah.common.repos.local.LocalTokenRepo

object Startup {
    implicit val config = ConfigFactory.load

    val equipmentClient =
        new DefaultHttpClient(
          ServiceTag.Monitoring,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("equipmentUrl")
        )

    val monitoringClient =
        new DefaultHttpClient(
          ServiceTag.Monitoring,
          Credentials(
            config.getString("serviceId"),
            config.getString("secret")
          ),
          config.getString("monitoringUrl")
        )

    implicit val pgdb = Database.forConfig("postgres")
    implicit val dbProfile = PostgresProfile
    implicit val context = new ReportingContext

    implicit val monitorRepo = new SlickOperationRecordRepo
    implicit val tokenRepo = new LocalTokenRepo
    implicit val userRepo = new SlickUserRepo
    implicit val dbManager = new SlickDbManager

    implicit val defaultOperationRecordService =
        new DefaultOperationRecordService[Future, DBIO]
    implicit val defaultStatService =
        new DefaultStatService[Future, DBIO](equipmentClient, monitoringClient)
    implicit val defaultTokenService = new DefaultTokenService[Future, DBIO]

    def startup() = Await.result(dbManager.execute(context.setup))

    implicit val actorSystem = ActorSystem("such-system")
    val rabbitControl = actorSystem.actorOf(Props[RabbitControl]())
    implicit val recoveryStrategy = RecoveryStrategy.none

    val subscriptionRef = Subscription.run(rabbitControl) {
        import Directives._

        channel(qos = 3) {
            consume(
              Queue.passive(
                queue(config.getString("reportingQueue"))
              )
            ) {
                (body(as[RecordRequest])) { (record) =>
                    defaultOperationRecordService.add(record)
                    ack
                }
            }
        }
    }
}
