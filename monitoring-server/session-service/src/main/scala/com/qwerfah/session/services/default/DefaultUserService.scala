package com.qwerfah.session.services.default

import cats.Monad
import cats.implicits._

import scala.util.Success
import scala.util.Failure

import java.security.MessageDigest

import com.qwerfah.session.repos.UserRepo
import com.qwerfah.session.services.UserService
import com.qwerfah.common.services.ServiceResponse
import com.qwerfah.common.Uid
import com.qwerfah.session.resources._
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.services._
import com.qwerfah.session.Mappings
import com.qwerfah.session.models.User
import com.qwerfah.common.randomUid
import com.qwerfah.common.models.Token
import com.qwerfah.common.exceptions.NoUserException

class DefaultUserService[F[_]: Monad, DB[_]: Monad](implicit
  userRepo: UserRepo[DB],
  dbManager: DbManager[F, DB],
  tokenService: TokenService[F]
) extends UserService[F] {
    import Mappings._

    override def register(
      request: UserRequest
    ): F[ServiceResponse[UserResponse]] = for {
        result <- dbManager.execute(userRepo.add(request))
    } yield ObjectResponse(result)

    override def login(
      credentials: Credentials
    ): F[ServiceResponse[Token]] = {
        val passwordHash = MessageDigest
            .getInstance("MD5")
            .digest(credentials.password.getBytes("UTF-8"))

        dbManager.execute(userRepo.getByLogin(credentials.login)) flatMap {
            case Some(user) if user.password.deep == passwordHash.deep =>
                tokenService.generate(user.uid.toString)
            case _ => Monad[F].pure(EmptyResponse)
        }
    }

    override def refresh(uid: String): F[ServiceResponse[Token]] =
        dbManager.execute(
          userRepo.getByUid(java.util.UUID.fromString(uid))
        ) flatMap {
            case Some(user) => tokenService.generate(uid)
            case None       => Monad[F].pure(EmptyResponse)
        }

    override def getAll: F[ServiceResponse[Seq[UserResponse]]] =
        for { users <- dbManager.execute(userRepo.get) } yield ObjectResponse(
          users
        )

    override def get(uid: Uid): F[ServiceResponse[UserResponse]] = for {
        user <- dbManager.execute(userRepo.getByUid(uid))
    } yield user match {
        case Some(user) => ObjectResponse(user)
        case None       => EmptyResponse
    }

    override def update(
      uid: Uid,
      request: UserRequest
    ): F[ServiceResponse[String]] = {
        val user: User = request
        for {
            result <- dbManager.execute(userRepo.update(user.copy(uid = uid)))
        } yield result match {
            case 1 => StringResponse("User updated")
            case _ => EmptyResponse
        }
    }

    override def remove(uid: Uid): F[ServiceResponse[String]] = {
        for {
            result <- dbManager.execute(userRepo.removeByUid(uid))
        } yield result match {
            case 1 => StringResponse("User removed")
            case _ => EmptyResponse
        }
    }
}
