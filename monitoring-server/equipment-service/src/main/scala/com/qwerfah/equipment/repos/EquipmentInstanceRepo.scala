package com.qwerfah.equipment.repos

import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.common.Uid

/** Equipment instance repository DSL. */
trait EquipmentInstanceRepo[DB[_]] {

    /** Get all equipment instances in repository.
      * @return
      *   All equipment instances in repository.
      */
    def get: DB[Seq[EquipmentInstance]]

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
    def getByUid(uid: Uid): DB[Option[EquipmentInstance]]

    /** GEt all equipment instances associated with specified equipment model.
      * @param modelUid
      *   Equipment model uid.
      * @return
      *   Equipment instances of the given model.
      */
    def getByModelUid(modelUid: Uid): DB[Seq[EquipmentInstance]]

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

    /** Remove equipment instance by its internal id. Soft delete - not actually
      * remove but only set isDeleted flag.
      * @param id
      *   Internal instance id.
      * @return
      *   Number of storage records affected (1 if successfull, otherwise 0).
      */
    def removeById(id: Int): DB[Int]

    /** Remove equipment instance by its external uid. Soft delete - not
      * actually remove but only set isDeleted flag.
      * @param id
      *   External instance uid.
      * @return
      *   Number of storage records affected (1 if successfull, otherwise 0).
      */
    def removeByUid(uid: Uid): DB[Int]

    /** Remove equipment instances of specified equipment model. Soft delete -
      * not actually remove but only set isDeleted flag.
      * @param id
      *   External instance uid.
      * @return
      *   Number of storage records affected (1 if successfull, otherwise 0).
      */
    def removeByModelUid(modelUid: Uid): DB[Int]

    /** Restore equipment instance that wasn't deleted permanently.
      * @param uid
      *   External equipment instance identifier.
      * @return
      *   Number of records in storage affected (1 if successfull, otherwise 0).
      */
    def restoreByUid(uid: Uid): DB[Int]

    /** Restore equipment instances of specified equipment model that weren't
      * deleted permanently.
      * @param uid
      *   External equipment instance identifier.
      * @return
      *   Number of records in storage affected (1 if successfull, otherwise 0).
      */
    def restoreByModelUid(modelUid: Uid): DB[Int]
}

/** Defines apply method to get implicitily defined equipment instance
  * repository DSL enterpreter from current scope.
  */
object EquipmentInstanceRepo {
    def apply[DB[_]](implicit
      repo: EquipmentInstanceRepo[DB]
    ): EquipmentInstanceRepo[DB] = repo
}
