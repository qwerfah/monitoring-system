package com.qwerfah.equipment.repos

import com.qwerfah.equipment.models._

/** Equipment model params repository DSL. */
trait ParamRepo[DB[_]] {

    /** Get model parameter by its internal id.
      * @param id
      *   Internal parameter id.
      * @return
      *   Parameter with specified id or None if not found.
      */
    def getById(id: Int): DB[Option[Param]]

    /** Get model parameter by its external uid.
      * @param id
      *   External parameter uid.
      * @return
      *   Parameter with specified uid or None if not found.
      */
    def getByGuid(uid: Uid): DB[Option[Param]]

    /** Add new model parameter to storage.
      * @param param
      *   New model parameter.
      * @return
      *   Added model parameter with internal id set by storage provider.
      */
    def add(param: Param): DB[Param]

    /** Update existing parameter with new field values.
      * @param param
      *   Parameter with new field values.
      * @return
      *   Number of records in storage affected (1 if successfull, otherwise 0).
      */
    def update(param: Param): DB[Int]

    /** Remove model parameter by its internal id.
      * @param id
      *   Internal parameter id.
      * @return
      *   Number of records in storage affected (1 if successfull, otherwise 0).
      */
    def removeById(id: Int): DB[Int]

    /** Remove model parameter by its external uid.
      * @param uid
      *   External parameter uid.
      * @return
      *   Number of records in storage affected (1 if successfull, otherwise 0).
      */
    def removeByGuid(uid: Uid): DB[Int]
}

/** Defines apply method to get implicitily defined model parameters repository
  * DSL enterpreter from current scope.
  */
object ParamRepo {
    def apply[DB[_]](implicit repo: ParamRepo[DB]) = repo
}
