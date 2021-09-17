package com.qwerfah.session.services.default

import cats.Monad
import cats.implicits._

import scala.util.Success
import scala.util.Failure

import java.time.Instant
import pdi.jwt.{JwtCirce, JwtAlgorithm, JwtClaim}

import com.qwerfah.session.repos.UserRepo
import com.qwerfah.session.services.UserService
import com.qwerfah.common.services.ServiceResponse
import com.qwerfah.common.Uid
import com.qwerfah.session.resources._
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.services._
import com.qwerfah.session.Mappings
import com.qwerfah.session.models.User
import java.security.MessageDigest

object JwtOptions {
    val key = "secretKey"
    val algorithm = JwtAlgorithm.HS256
    val accessExpiration = Some(
      Instant.now.plusSeconds(157784760).getEpochSecond
    )
    val refreshExpiration = Some(
      Instant.now.plusSeconds(157784760).getEpochSecond
    )
    val issuedAt = Some(Instant.now.getEpochSecond)
}

class DefaultUserService[F[_]: Monad, DB[_]: Monad](implicit
  userRepo: UserRepo[DB],
  dbManager: DbManager[F, DB]
) extends UserService[F] {
    import Mappings._

    private def generateToken(userUid: Uid, expiration: Some[Long]): String = {
        val claim = JwtClaim(
          content = userUid.toString,
          expiration = expiration,
          issuedAt = JwtOptions.issuedAt
        )

        JwtCirce.encode(claim, JwtOptions.key, JwtOptions.algorithm)
    }

    private def generateToken(userUid: Uid): Token = Token(
      generateToken(userUid, JwtOptions.accessExpiration),
      generateToken(userUid, JwtOptions.refreshExpiration)
    )

    override def register(
      request: UserRequest
    ): F[ServiceResponse[UserResponse]] = for {
        result <- dbManager.execute(userRepo.add(request))
    } yield ObjectResponse(result)

    override def login(
      credentials: Credentials
    ): F[ServiceResponse[Token]] = {
        val passwordHash =
            MessageDigest
                .getInstance("MD5")
                .digest(credentials.password.getBytes)
        dbManager.execute(userRepo.getByLogin(credentials.login)) map {
            case Some(user) if user.password == passwordHash => {
                ObjectResponse(generateToken(user.uid))
            }
            case _ => EmptyResponse
        }
    }

    override def refresh(token: String): F[ServiceResponse[Token]] = {
        val decoded = JwtCirce.decode(
          token,
          JwtOptions.key,
          Seq(JwtOptions.algorithm)
        )

        decoded match {
            case Success(value) =>
                dbManager.execute(
                  userRepo.getByUid(java.util.UUID.fromString(value.content))
                ) map {
                    case Some(value) => ObjectResponse(generateToken(value.uid))
                    case None        => EmptyResponse
                }
            case Failure(error) => Monad[F].pure(ErrorResponse(error))
        }
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
            case 1 => ObjectResponse("User updated")
            case _ => EmptyResponse
        }
    }

    override def remove(uid: Uid): F[ServiceResponse[String]] = {
        for {
            result <- dbManager.execute(userRepo.removeByUid(uid))
        } yield result match {
            case 1 => ObjectResponse("User removed")
            case _ => EmptyResponse
        }
    }
}
