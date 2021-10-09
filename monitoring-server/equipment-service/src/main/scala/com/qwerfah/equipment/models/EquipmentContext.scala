package com.qwerfah.equipment.models

import java.security.MessageDigest

import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global

import enumeratum._

import com.typesafe.config.{Config, ConfigObject}

import io.circe.generic.auto._
import io.circe.config.syntax._

import com.qwerfah.equipment.resources._
import com.qwerfah.common.{Uid, randomUid, hashString, uidFromString}
import com.qwerfah.common.models._
import com.qwerfah.common.resources.{UserRole, Credentials}

/** Database scheme context base on jdbc profile provided.
  * @param jdbcProfile
  *   Current jdbc profile for interaction with db provider.
  */
class EquipmentContext(implicit jdbcProfile: JdbcProfile, config: Config)
  extends DataContext(jdbcProfile) {
    import profile.api._

    implicit lazy val statusMapper = mappedColumnTypeForEnum(EquipmentStatus)

    /** Equipment models table definition.
      * @param tag
      *   Table tag.
      */
    final class EquipmentModelTable(tag: Tag)
      extends Table[EquipmentModel](tag, "EQUIPMENT_MODELS") {

        /** Internal model identifier. */
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        /** External model identifier. */
        def uid = column[Uid]("GUID", O.Unique)

        /** Model name. */
        def name = column[String]("NAME")

        /** Model description (optional). */
        def description = column[Option[String]]("DESCRIPTION")

        /** Shows if soft delete applied to the record in table. */
        def isDeleted = column[Boolean]("IS_DELETED")

        def * = (
          id.?,
          uid,
          name,
          description,
          isDeleted
        ).<>(EquipmentModel.tupled, EquipmentModel.unapply)
    }

    /** Equipment model parameters table definition.
      * @param tag
      *   Table tag.
      */
    final class ParamTable(tag: Tag) extends Table[Param](tag, "PARAMS") {

        /** Internal model parameter identifier. */
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        /** External model parameter identifier. */
        def uid = column[Uid]("UID", O.Unique)

        /** Internal model identifier assosiated with current parameter. */
        def modelUid = column[Uid]("MODEL_UID")

        /** Model param name. */
        def name = column[String]("NAME")

        /** Model param measurment units (optional). */
        def measurmentUnits = column[Option[String]]("MEASURMENT_UNITS")

        /** Shows if soft delete applied to the record in table. */
        def isDeleted = column[Boolean]("IS_DELETED")

        def * = (
          id.?,
          uid,
          modelUid,
          name,
          measurmentUnits,
          isDeleted
        ).<>(Param.tupled, Param.unapply)

        def model = foreignKey("MODEL_FK", modelUid, models)(_.uid)
    }

    /** Equipment instances table definition.
      * @param tag
      *   Table tag.
      */
    final class EquipmentInstanceTable(tag: Tag)
      extends Table[EquipmentInstance](
        tag,
        "EQUIPMENT_INSTANCES"
      ) {

        /** Equipment status enum to string mapper. */
        implicit val statusMapper =
            MappedColumnType.base[EquipmentStatus, String](
              e => e.toString,
              s => EquipmentStatus.withName(s)
            )

        /** Internal instance identifier. */
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        /** External instance identifier. */
        def uid = column[Uid]("UID", O.Unique)

        /** Internal id of equipment model associated with current instance. */
        def modelUid = column[Uid]("MODEL_UID")

        /** External id of equipment model associated with current instance. */
        // def modelUid = column[UUID]("MODEL_UID")

        /** Instance name. */
        def name = column[String]("NAME")

        /** Instance description. */
        def description = column[Option[String]]("DESCRIPTION")

        /** Equipment instance status. */
        def status = column[EquipmentStatus]("STATUS")

        /** Shows if soft delete applied to the record in table. */
        def isDeleted = column[Boolean]("IS_DELETED")

        def * = (
          id.?,
          uid,
          modelUid,
          name,
          description,
          status,
          isDeleted
        ).<>(EquipmentInstance.tupled, EquipmentInstance.unapply)

        def model = foreignKey("MODEL_FK", modelUid, models)(_.uid)
    }

    /** Equipment models table query instance. */
    val models = TableQuery[EquipmentModelTable]

    /** Equipment model params table query instance. */
    val params = TableQuery[ParamTable]

    /** Equipment instances table query instance. */
    val instances = TableQuery[EquipmentInstanceTable]

    /** Db schema initialization.2 */
    val setup = DBIO.seq(
      // Create db schema
      models.schema
          .++(params.schema)
          .++(instances.schema)
          .++(users.schema)
          .createIfNotExists,
      // Insert equipment models
      models.exists.result flatMap { exisits =>
          if (!exisits) models ++= initialModels
          else DBIO.successful(None)
      },
      // Insert model params
      params.exists.result flatMap { exisits =>
          if (!exisits) params ++= initialParams
          else DBIO.successful(None)
      },
      // Insert equipment instances
      instances.exists.result flatMap { exists =>
          if (!exists) instances ++= initialInstances
          else DBIO.successful(None)
      },
      // Insert equipment instances
      users.exists.result flatMap { exists =>
          if (!exists) addUsers
          else DBIO.successful(None)
      }
    )

    private val modelUids = Seq(
      uidFromString("c8eaa43e-093c-48ff-b140-090a006cc882"),
      uidFromString("a2c489fa-5f78-4ff6-9f78-7fa8a5e5a918"),
      uidFromString("e6910ffd-8fbd-456c-a371-159117df9fbf")
    )

    private val paramUids = Seq(
      uidFromString("050d4139-d59b-4abb-aa33-a6d09f062be2"),
      uidFromString("9b00e6a2-bb3d-4de0-b513-88137ab287e7"),
      uidFromString("6c552410-f586-4348-a617-0a175397733d")
    )

    private val instanceUids = Seq(
      uidFromString("c9686e98-0a32-4084-b883-7ea2f6334df0"),
      uidFromString("6c4cd202-e693-4245-8cca-6b2ff430e9b3"),
      uidFromString("a2fc3c37-3873-43d2-a662-3254632253f4")
    )

    private val initialModels = Seq(
      EquipmentModel(
        Some(1),
        modelUids(0),
        "Model_1",
        Some("Description of Model_1"),
        false
      ),
      EquipmentModel(
        Some(2),
        modelUids(1),
        "Model_2",
        Some("Description of Model_2"),
        false
      ),
      EquipmentModel(
        Some(3),
        modelUids(2),
        "Model_3",
        Some("Description of Model_3"),
        false
      )
    )

    private val initialParams = Seq(
      Param(
        Some(1),
        paramUids(0),
        modelUids(0),
        "Param_1",
        Some("m"),
        false
      ),
      Param(
        Some(2),
        paramUids(1),
        modelUids(1),
        "Param_2",
        Some("kg"),
        false
      ),
      Param(
        Some(3),
        paramUids(2),
        modelUids(2),
        "Param_3",
        Some("sec"),
        false
      )
    )

    private val initialInstances = Seq(
      EquipmentInstance(
        Some(1),
        instanceUids(0),
        modelUids(0),
        "Instance_1",
        Some("Description of Instance_1"),
        EquipmentStatus.Active,
        false
      ),
      EquipmentInstance(
        Some(2),
        instanceUids(1),
        modelUids(1),
        "Instance_2",
        Some("Description of Instance_2"),
        EquipmentStatus.Active,
        false
      ),
      EquipmentInstance(
        Some(3),
        instanceUids(2),
        modelUids(2),
        "Instance_3",
        Some("Description of Instance_3"),
        EquipmentStatus.Active,
        false
      )
    )

    private def addUsers = {
        val services = collection.mutable.ListBuffer[User]()

        for (
          creds <- config.getObjectList("serviceCredentials").toArray;
          cred <- creds.asInstanceOf[ConfigObject].toConfig.as[Credentials]
        )
            services += User(
              None,
              randomUid,
              cred.login,
              hashString(cred.password),
              UserRole.Service,
              false
            )

        DBIO.seq(users ++= services)
    }
}
