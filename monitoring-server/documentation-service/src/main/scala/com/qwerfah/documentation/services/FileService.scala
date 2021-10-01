package com.qwerfah.documentation.services

import com.qwerfah.documentation.resources._
import com.qwerfah.documentation.models.File

import com.qwerfah.common.Uid
import com.qwerfah.common.services.response.{ServiceResponse, ResponseMessage}

trait FileService[F[_]] {

    /** Get all files in storage.
      * @return
      *   Collection of all files in storage or error response in case of
      *   failure.
      */
    def get: F[ServiceResponse[Seq[FileMetaResponse]]]

    /** Get file by its external identifier.
      * @param uid
      *   External file identifier.
      * @return
      *   File with specified identifier or error in case of failure.
      */
    def get(uid: Uid): F[ServiceResponse[FileResponse]]

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
      *   Number of records in repo affected or error in case failure.
      */
    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]
}
