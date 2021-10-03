package com.qwerfah.documentation.repos

import com.qwerfah.documentation.models.{File, FileMeta}

import com.qwerfah.common.Uid

/** Basic files repo functionality. */
trait FileRepo[DB[_]] {

    /** Get all files metadata in repo.
      * @return
      *   Collection of all files in repo.
      */
    def getMeta: DB[Seq[FileMeta]]

    /** Get all files metadata in repo for specified equipment model.
      * @return
      *   Collection of all files metadata in repo associated with given model.
      */
    def getModelMeta(modelUid: Uid): DB[Seq[FileMeta]]

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
      *   Added file metadata instance with internal identifier defined by
      *   storage provider.
      */
    def add(file: File): DB[FileMeta]

    /** Remove file by its external identifier.
      * @param uid
      *   File external identifier.
      * @return
      *   Number of records in repo affected.
      */
    def remove(uid: Uid): DB[Int]

    /** Remove all files for specified equipment model.
      * @param uid
      *   Equipment model identifier.
      * @return
      *   Number of records in repo affected.
      */
    def removeModelFiles(modelUid: Uid): DB[Int]
}
