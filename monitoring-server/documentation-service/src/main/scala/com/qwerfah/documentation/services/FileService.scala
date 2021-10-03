package com.qwerfah.documentation.services

import com.qwerfah.documentation.resources._
import com.qwerfah.documentation.models.File

import com.qwerfah.common.Uid
import com.qwerfah.common.services.response.{ServiceResponse, ResponseMessage}

trait FileService[F[_]] {

    /** Get all files metadata in storage.
      * @return
      *   Collection of all files metadate in storage or error response in case
      *   of failure.
      */
    def getMeta: F[ServiceResponse[Seq[FileMetaResponse]]]

    /** Get file content by its external identifier.
      * @param uid
      *   External file identifier.
      * @return
      *   File content with specified identifier or error in case of failure.
      */
    def get(uid: Uid): F[ServiceResponse[FileResponse]]

    /** Get files metadata for all files belong to the specified equipment
      * model.
      * @param modelUid
      *   Equipment model identifier.
      * @return
      *   Files metadata for given model uid.
      */
    def getModelMeta(
      modelUid: Uid
    ): F[ServiceResponse[Seq[FileMetaResponse]]]

    /** Add new file into repo.
      * @param file
      *   New file.
      * @return
      *   Added file instance with internal identifier defined by storage
      *   provider or error in case of failure.
      */
    def add(file: FileRequest): F[ServiceResponse[FileMetaResponse]]

    /** Remove file by its external identifier.
      * @param uid
      *   File external identifier.
      * @return
      *   Description of operation result.
      */
    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]

    /** Remove all files for specified model.
      * @param modelUid
      *   Equipment model identifier.
      * @return
      *   Description of operation result.
      */
    def removeModelFiles(modelUid: Uid): F[ServiceResponse[ResponseMessage]]
}
