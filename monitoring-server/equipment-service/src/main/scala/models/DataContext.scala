package models

import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global

/** Database scheme context base on jdbc profile provided.
  * @param jdbcProfile
  *   Current jdbc profile for interaction with db provider.
  */
class DataContext(val jdbcProfile: JdbcProfile) {
    import jdbcProfile.api._

    /** Equipment models table definition.
      * @param tag
      *   Table tag.
      */
    final class EquipmentModelTable(tag: Tag)
      extends Table[EquipmentModel](tag, "EQUIPMENT_MODELS") {

        /** Internal model identifier. */
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        /** External model identifier. */
        def uid = column[Guid]("GUID", O.Unique)

        /** Model name. */
        def name = column[String]("NAME")

        /** Model description (optional). */
        def description = column[String]("DESCRIPTION")

        def * = (
          id.?,
          uid,
          name,
          description.?
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
        def uid = column[Guid]("UID", O.Unique)

        /** Internal model identifier assosiated with current parameter. */
        def modelId = column[Int]("MODEL_ID")

        /** Model param name. */
        def name = column[String]("NAME")

        /** Model param measurment units (optional). */
        def measurmentUnits = column[String]("MEASURMENT_UNITS")

        def * = (
          id.?,
          uid,
          modelId,
          name,
          measurmentUnits.?
        ).<>(Param.tupled, Param.unapply)

        def model = foreignKey("MODEL_FK", modelId, models)(_.id)
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
        def uid = column[Guid]("UID", O.Unique)

        /** Internal id of equipment model associated with current instance. */
        def modelId = column[Int]("MODEL_ID")

        /** External id of equipment model associated with current instance. */
        // def modelUid = column[UUID]("MODEL_UID")

        /** Instance name. */
        def name = column[String]("NAME")

        /** Instance description. */
        def description = column[String]("DESCRIPTION")

        /** Equipment instance status. */
        def status = column[EquipmentStatus]("STATUS")

        def * = (
          id.?,
          uid,
          modelId,
          name,
          description.?,
          status
        ).<>(EquipmentInstance.tupled, EquipmentInstance.unapply)
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
      (models.schema ++ params.schema ++ instances.schema).createIfNotExists,
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
      }
    )

    private val initialModels = Seq(
      EquipmentModel(
        Some(1),
        randomGuid,
        "Model_1",
        Some("Description of Model_1")
      ),
      EquipmentModel(
        Some(2),
        randomGuid,
        "Model_2",
        Some("Description of Model_2")
      ),
      EquipmentModel(
        Some(3),
        randomGuid,
        "Model_3",
        Some("Description of Model_3")
      )
    )

    private val initialParams = Seq(
      Param(Some(1), randomGuid, 1, "Param_1", Some("m")),
      Param(Some(2), randomGuid, 2, "Param_2", Some("kg")),
      Param(Some(3), randomGuid, 3, "Param_3", Some("sec"))
    )

    private val initialInstances = Seq(
      EquipmentInstance(
        Some(1),
        randomGuid,
        1,
        "Instance_1",
        Some("Description of Instance_1"),
        EquipmentStatus.Active
      ),
      EquipmentInstance(
        Some(2),
        java.util.UUID.randomUUID,
        2,
        "Instance_2",
        Some("Description of Instance_2"),
        EquipmentStatus.Active
      ),
      EquipmentInstance(
        Some(3),
        java.util.UUID.randomUUID,
        3,
        "Instance_3",
        Some("Description of Instance_3"),
        EquipmentStatus.Active
      )
    )
}
