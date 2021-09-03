package models

import slick.jdbc.JdbcProfile

class DataContext(val jdbcProfile: JdbcProfile) {
    import jdbcProfile.api._

    final class EquipmentModelTable(tag: Tag)
      extends Table[EquipmentModel](tag, "EQUIPMENT_MODEL") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
        def uid = column[Guid]("GUID", O.Unique)
        def name = column[String]("NAME")
        def description = column[String]("DESCRIPTION")
        def * = (
          id.?,
          uid,
          name,
          description.?
        ) <> (EquipmentModel.tupled, EquipmentModel.unapply)
    }

    final class ParamTable(tag: Tag) extends Table[Param](tag, "PARAMS") {
        def id = column[Int]("ID")
        def uid = column[Guid]("UID")
        def modelId = column[Int]("MODEL_ID")
        def name = column[String]("NAME")
        def measurmentUnits = column[String]("MEASURMENT_UNITS")

        def * = (
          id.?,
          uid,
          modelId,
          name,
          measurmentUnits.?
        ) <> (Param.tupled, Param.unapply)

        // def model = foreignKey("MODEL_FK", modelId, models)(_.id)
    }

    /** Equipment instance db model.
      * @param tag
      *   Table tag.
      * @param models
      *   Query instance for foreign key to the equipment model table.
      */
    final class EquipmentInstanceTable(tag: Tag)
      extends Table[EquipmentInstance](
        tag,
        "EQUIPMENT"
      ) {

        /** Equipment status enum to string mapper. */
        implicit val statusMapper =
            MappedColumnType.base[EquipmentStatus, String](
              e => e.toString,
              s => EquipmentStatus.withName(s)
            )

        /** Internal instance identifier. */
        def id = column[Int]("ID", O.PrimaryKey)

        /** External instance identifier. */
        def uid = column[Guid]("UID")

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
        ) <> (EquipmentInstance.tupled, EquipmentInstance.unapply)

    }

    /** Equipment models table query instance. */
    val models = TableQuery[EquipmentModelTable]

    /** Equipment model params table query instance. */
    val params = TableQuery[ParamTable]

    /** Equipment instances table query instance. */
    val instances = TableQuery[EquipmentInstanceTable]

    val setup = DBIO.seq(
      // Create db schema
      (models.schema ++ params.schema ++ instances.schema).create,
      // Insert equipment models
      models ++= Seq(
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
      ),
      // Insert model params
      params ++= Seq(
        Param(Some(1), randomGuid, 1, "Param_1", Some("m")),
        Param(Some(2), randomGuid, 2, "Param_2", Some("kg")),
        Param(Some(3), randomGuid, 3, "Param_3", Some("sec"))
      ),
      // Insert equipment instances
      instances ++= Seq(
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
    )
}
