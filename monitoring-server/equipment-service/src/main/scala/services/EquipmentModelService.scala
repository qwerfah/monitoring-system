package services

import models._
import slick.lifted.TableQuery

trait EquipmentModelService[F[_]] {
    def get: F[ServiceResponse[Seq[EquipmentModel]]]
    def getById(id: Int): F[ServiceResponse[EquipmentModel]]
    def getByGuid(uid: Uid): F[ServiceResponse[EquipmentModel]]
    def add(model: EquipmentModel): F[ServiceResponse[EquipmentModel]]
    def update(uid: Uid, model: EquipmentModel): F[ServiceResponse[String]]
    def removeById(id: Int): F[ServiceResponse[String]]
    def removeByGuid(uid: Uid): F[ServiceResponse[String]]
}
