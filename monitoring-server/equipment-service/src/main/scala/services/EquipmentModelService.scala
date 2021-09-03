package services

import models._
import slick.lifted.TableQuery

trait EquipmentModelService[F[_]] {
    def getById(id: Int): F[Option[EquipmentModel]]
}
