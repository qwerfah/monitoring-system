package com.qwerfah.documentation.repos

import com.qwerfah.documentation.models.File

import com.qwerfah.common.Uid

/** Basic files repo functionality. */
trait FileRepo[DB[_]] {

    /** Get all files in repo.
      * @return
      *   Collection of all files in repo.
      */
    def get: DB[Seq[File]]

    /** Get file by its external identifier.
      * @param uid
      *   External file identifier.
      * @return
      *   File with specified identifier or None if not found.
      */
    def get(uid: Uid): DB[Option[File]]

    /** Add new file into repo.
      * @param file
      *   New file.
      * @return
      *   Added file instance with internal identifier defined by storage
      *   provider.
      */
    def add(file: File): DB[File]

    /** Remove file by its external identifier.
      * @param uid
      *   File external identifier.
      * @return
      *   Number of records in repo affected.
      */
    def remove(uid: Uid): DB[Int]
}
