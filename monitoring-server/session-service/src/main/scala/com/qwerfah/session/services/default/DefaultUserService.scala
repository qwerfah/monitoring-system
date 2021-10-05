package com.qwerfah.session.services.default

import cats.Monad
import cats.implicits._

import scala.util.{Success, Failure}

import java.security.MessageDigest

import com.qwerfah.session.services.UserService

import com.qwerfah.common.repos.UserRepo
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid
import com.qwerfah.common.resources._
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.Mappings
import com.qwerfah.common.randomUid
import com.qwerfah.common.models._
import com.qwerfah.common.exceptions._
import com.qwerfah.common.util.Conversions._
import com.qwerfah.common.repos.TokenRepo

class DefaultUserService[F[_]: Monad, DB[_]: Monad](implicit
  userRepo: UserRepo[DB],
  tokenRepo: TokenRepo[DB],
  dbManager: DbManager[F, DB]
) extends UserService[F] {
    import Mappings._

    override def register(
      request: UserRequest
    ): F[ServiceResponse[UserResponse]] = for {
        user <- dbManager.execute(userRepo.add(request.asUser))
    } yield user.asResponse.as201

    override def getAll: F[ServiceResponse[Seq[UserResponse]]] =
        for {
            users <- dbManager.execute(userRepo.get)
        } yield users.asResponse.as200

    override def get(uid: Uid): F[ServiceResponse[UserResponse]] = for {
        user <- dbManager.execute(userRepo.getByUid(uid))
    } yield user match {
        case Some(user) => user.asResponse.as200
        case None       => NoUser(uid).as404
    }

    override def update(
      uid: Uid,
      request: UserRequest
    ): F[ServiceResponse[ResponseMessage]] = {
        for {
            result <- dbManager.execute(
              userRepo.update(request.asUser.copy(uid = uid))
            )
        } yield result match {
            case 1 => UserUpdated(uid).as200
            case _ => NoUser(uid).as404
        }
    }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] = {
        dbManager.execute(userRepo.removeByUid(uid)) flatMap {
            case 1 =>
                dbManager.execute(tokenRepo.removeByUid(uid)) map { _ =>
                    UserRemoved(uid).as200
                }
            case _ => Monad[F].pure(NoUser(uid).as404)
        }
    }

    def restore(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(userRepo.restoreByUid(uid)) map {
            case 1 => UserRestored(uid).as200
            case _ => NoUser(uid).as404
        }
}
