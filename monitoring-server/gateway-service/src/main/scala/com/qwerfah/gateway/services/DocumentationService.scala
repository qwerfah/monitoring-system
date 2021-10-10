package com.qwerfah.gateway.services

import com.twitter.finagle.http.{Request, Response}

import com.qwerfah.common.Uid

/** Provide basic remote documentation service access operations. */
trait DocumentationService[F[_]] {

    /** Get all files in documentation service storage.
      * @return
      *   Response from documentation service.
      */
    def getFiles: F[Response]

    /** Get all files for specified equipment model.
      * @param modelUid
      *   Equipment model identifier.
      * @return
      *   Response from documentation service.
      */
    def getModelFiles(modelUid: Uid): F[Response]

    /** Get specified file content.
      * @param uid
      *   Documentation file identifier.
      * @return
      *   Response from documentation service.
      */
    def getFile(uid: Uid): F[Response]

    /** Add new file for the specifier equipment model to the documentation
      * service storage.
      * @param modelUid
      *   Equipment model identifier.
      * @param request
      *   Request instance with new file content.
      * @return
      *   Response from documentation service.
      */
    def addFile(modelUid: Uid, request: Request): F[Response]

    /** Remove file from documentation service storage.
      * @param uid
      *   File identifier.
      * @return
      *   Response from documentation service.
      */
    def removeFile(uid: Uid): F[Response]
}
