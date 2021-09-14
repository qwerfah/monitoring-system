package com.qwerfah.equipment

import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._

object Mappings {
    implicit def requestToModel(request: ModelRequest): EquipmentModel =
        EquipmentModel(None, randomUid, request.name, request.description)

    implicit def modelToResponse(model: EquipmentModel): ModelResponse =
        ModelResponse(model.uid, model.name, model.description)

    implicit def modelsToResponses(
      models: Seq[EquipmentModel]
    ): Seq[ModelResponse] =
        for { model <- models } yield ModelResponse(
          model.uid,
          model.name,
          model.description
        )

    implicit def requestToInstance(
      request: InstanceRequest
    ): EquipmentInstance =
        EquipmentInstance(
          None,
          randomUid,
          request.modelUid,
          request.name,
          request.description,
          request.status
        )

    implicit def instanceToResponse(
      instance: EquipmentInstance
    ): InstanceResponse =
        InstanceResponse(
          instance.uid,
          instance.modelUid,
          instance.name,
          instance.description,
          instance.status
        )

    implicit def instancesToResponses(
      instances: Seq[EquipmentInstance]
    ): Seq[InstanceResponse] =
        for { instance <- instances } yield InstanceResponse(
          instance.uid,
          instance.modelUid,
          instance.name,
          instance.description,
          instance.status
        )
}
