package com.qwerfah.common.repos

import com.qwerfah.common.models._
import com.qwerfah.common.Uid

/** Users repository DSL. */
trait UserRepo[DB[_]] {

    /** Get all users in repository.
      * @return
      *   All users in repository.
      */
    def get: DB[Seq[User]]

    /** Get user by its internal id.
      * @param id
      *   Internal user id.
      * @return
      *   User with specified id or None if not found.
      */
    def getById(id: Int): DB[Option[User]]

    /** Get user by its external uid.
      * @param id
      *   External user uid.
      * @return
      *   User with specified uid or None if not found.
      */
    def getByUid(uid: Uid): DB[Option[User]]

    /** Get user by its unique login.
      * @param id
      *   Unique user login.
      * @return
      *   User with specified login or None if not found.
      */
    def getUserByLogin(login: String): DB[Option[User]]

    /** Get service user record by its unique login.
      * @param id
      *   Unique user login.
      * @return
      *   Service user record with specified login or None if not found.
      */
    def getServiceByLogin(login: String): DB[Option[User]]

    /** Add new user to storage.
      * @param user
      *   New user.
      * @return
      *   Added user with internal id set by storage provider.
      */
    def add(user: User): DB[User]

    /** Update existing user with new field values.
      * @param user
      *   User with new field values.
      * @return
      *   Number of records in storage affected (1 if successfull, otherwise 0).
      */
    def update(user: User): DB[Int]

    /** Remove user by its internal id (soft delete).
      * @param id
      *   Internal user id.
      * @return
      *   Number of records in storage affected (1 if successfull, otherwise 0).
      */
    def removeById(id: Int): DB[Int]

    /** Remove user by its external uid (soft delete).
      * @param uid
      *   External user uid.
      * @return
      *   Number of records in storage affected (1 if successfull, otherwise 0).
      */
    def removeByUid(uid: Uid): DB[Int]

    /** Restore user by its external uid if it wasn't removed permamently.
      * @param uid
      *   External user uid.
      * @return
      *   Number of records in storage affected (1 if successfull, otherwise 0).
      */
    def restoreByUid(uid: Uid): DB[Int]
}
