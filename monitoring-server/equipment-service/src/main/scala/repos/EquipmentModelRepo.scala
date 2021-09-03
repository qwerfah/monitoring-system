package repos

import models._

/** Equipment models repository DSL. */
trait EquipmentModelRepo[DB[_]] {

    /** Get equipment model by the internal id.
      * @param id
      *   Internal model id.
      * @return
      *   Equipment model with given id or None if not found.
      */
    def getById(id: Int): DB[Option[EquipmentModel]]

    /** Get equipment model by the external uid.
      * @param uid
      *   External model uid.
      * @return
      *   Equipment model with given uid or None if not found.
      */
    def getByUid(uid: Guid): DB[Option[EquipmentModel]]

    /** Add new equipment model to the storage.
      * @param model
      *   New equipment model.
      * @return
      *   Equipment model with internal id set by db provider.
      */
    def add(model: EquipmentModel): DB[EquipmentModel]

    /** Update exisiting equipment model.
      * @param model
      *   Model instance with updated field values.
      * @return
      *   Number of storage records affected (1 if successfull, otherwise 0).
      */
    def update(model: EquipmentModel): DB[Int]

    /** Remove model by its internal id.
      * @param id
      *   Internal model id.
      * @return
      *   Number of storage records affected (1 if successfull, otherwise 0).
      */
    def removeById(id: Int): DB[Int]

    /** Remove model by its external uid.
      * @param uid
      *   External model uid.
      * @return
      *   Number of storage records affected (1 if successfull, otherwise 0).
      */
    def removeByUid(uid: Guid): DB[Int]
}

/** Defines apply method to get implicitily defined equipment models repository
  * DSL enterpreter from current scope.
  */
object EquipmentModelRepo {
    def apply[DB[_]](implicit
      repo: EquipmentModelRepo[DB]
    ): EquipmentModelRepo[DB] = repo
}
