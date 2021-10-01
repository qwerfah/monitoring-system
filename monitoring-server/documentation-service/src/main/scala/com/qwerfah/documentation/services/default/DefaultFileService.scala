package com.qwerfah.documentation.services.default

import cats.Monad
import cats.implicits._

import com.qwerfah.documentation.Mappings._
import com.qwerfah.documentation.resources._
import com.qwerfah.documentation.repos.FileRepo
import com.qwerfah.documentation.services.FileService

import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.util.Conversions._
import com.qwerfah.common.services.response._

class DefaultFileService[F[_]: Monad, DB[_]: Monad](implicit
  fileRepo: FileRepo[DB],
  dbManager: DbManager[F, DB]
) extends FileService[F] {

    override def getMeta: F[ServiceResponse[Seq[FileMetaResponse]]] =
        dbManager.execute(fileRepo.getMeta) map { _.asResponse.as200 }

    override def get(uid: Uid): F[ServiceResponse[FileResponse]] =
        dbManager.execute(fileRepo.get(uid)) map {
            case Some(value) => value.asResponse.as200
            case None        => NoFile(uid).as404
        }

    override def getModelMeta(
      modelUid: Uid
    ): F[ServiceResponse[Seq[FileMetaResponse]]] =
        dbManager.execute(fileRepo.getModelMeta(modelUid)) map {
            _.asResponse.as200
        }
 
    override def add(file: FileRequest): F[ServiceResponse[FileMetaResponse]] =
        dbManager.execute(fileRepo.add(file.asFile)) map { _.asResponse.as201 }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(fileRepo.remove(uid)) map {
            case 1 => FileRemoved(uid).as200
            case _ => NoFile(uid).as404
        }
}
