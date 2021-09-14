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
}
