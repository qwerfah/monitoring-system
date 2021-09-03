package services.default

import scala.concurrent.ExecutionContext
import scalaz.{Monad, OptionT, ~>}

import repos._
import models._
import services._

class DefaultEquipmentModelService[F[_]: Monad, DB[_]: Monad](
  repo: EquipmentModelRepo[DB],
  execute: DB ~> F
) extends EquipmentModelService[F] {
    override def getById(id: Int): F[Option[EquipmentModel]] = execute(
      repo.getById(id)
    )
}
