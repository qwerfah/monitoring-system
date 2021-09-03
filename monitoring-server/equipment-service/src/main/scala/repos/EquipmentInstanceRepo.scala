package repos

import models._

/** Equipment instance repository DSL. */
trait EquipmentInstanceRepo[DB[_]] {

    /** Get equipment instance by its internal id.
      * @param id
      *   Internal equipment instance id.
      * @return
      *   Equipment instance with given id or None if not found.
      */
    def getById(id: Int): DB[Option[EquipmentInstance]]

    /** Get equipment instance by its external uid.
      * @param id
      *   External equipment instance uid.
      * @return
      *   Equipment instance with given uid or None if not found.
      */
    def getByGuid(uid: Guid): DB[Option[EquipmentInstance]]

    /** Add new instance to db table.
      * @param instance
      *   New equipment instance.
      * @return
      *   Equipment instance with internal id set by db provider.
      */
    def add(instance: EquipmentInstance): DB[EquipmentInstance]

    /** Update existing equipment intance.
      * @param instance
      *   Equipment instance with updated field values.
      * @return
      *   Number of storage records affected (1 if successfull, otherwise 0).
      */
    def update(instance: EquipmentInstance): DB[Int]

    /** Remove equipment instance by its internal id.
      * @param id
      *   Internal instance id.
      * @return
      *   Number of storage records affected (1 if successfull, otherwise 0).
      */
    def removeById(id: Int): DB[Int]

    /** Remove equipment instance by its external uid.
      * @param id
      *   External instance uid.
      * @return
      *   Number of storage records affected (1 if successfull, otherwise 0).
      */
    def removeByGuid(uid: Guid): DB[Int]
}

/** Defines apply method to get implicitily defined equipment instance
  * repository DSL enterpreter from current scope.
  */
object EquipmentInstanceRepo {
    def apply[DB[_]](implicit
      repo: EquipmentInstanceRepo[DB]
    ): EquipmentInstanceRepo[DB] = repo
}
