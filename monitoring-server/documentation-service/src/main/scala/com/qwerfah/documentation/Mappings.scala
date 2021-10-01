package com.qwerfah.documentation

import com.qwerfah.documentation.models.File
import com.qwerfah.documentation.resources._

import com.qwerfah.common.{randomUid}

object Mappings {
    implicit class RequestToFileMapping(r: FileRequest) {
        def asFile = File(
          None,
          randomUid,
          r.modelUid,
          r.filename,
          r.contentType,
          r.content
        )
    }

    implicit class FileToMetaResponseMapping(f: File) {
        def asMeta = FileMetaResponse(
          f.uid,
          f.modelUid,
          f.filename,
          f.contentType
        )
    }

    implicit class FileSeqToMetaResponseSeqMapping(files: Seq[File]) {
        def asMeta = for { f <- files } yield FileMetaResponse(
          f.uid,
          f.modelUid,
          f.filename,
          f.contentType
        )
    }

    implicit class FileToResponseMapping(f: File) {
        def asResponse = FileResponse(
          f.filename,
          f.contentType,
          f.content
        )
    }
}
