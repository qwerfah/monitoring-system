package com.qwerfah.equipment

import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.common.randomUid

/** Provides implicit mappings for different resources. */
object Mappings {

    /** Equipment model request to equipment model mapping.
      * @param request
      *   Equipment model request.
      * @return
      *   Equipment model.
      */
    implicit def requestToModel(request: ModelRequest): EquipmentModel =
        EquipmentModel(None, randomUid, request.name, request.description)

    /** Equipment model to equipment model response mapping.
      * @param model
      *   Equipment model.
      * @return
      *   Equipment model response.
      */
    implicit def modelToResponse(model: EquipmentModel): ModelResponse =
        ModelResponse(model.uid, model.name, model.description)

    /** Equipment model sequence to equipment model resource sequence mapping.
      * @param models
      *   Equipment model sequence.
      * @return
      *   Equipment model resource sequence.
      */
    implicit def modelsToResponses(
      models: Seq[EquipmentModel]
    ): Seq[ModelResponse] =
        for { model <- models } yield ModelResponse(
          model.uid,
          model.name,
          model.description
        )

    /** Equipment instance adding request to equipment instance mapping.
      * @param request
      *   Equipment instance adding request.
      * @return
      *   Equipment instance.
      */
    implicit def addRequestToInstance(
      request: AddInstanceRequest
    ): EquipmentInstance =
        EquipmentInstance(
          None,
          randomUid,
          request.modelUid,
          request.name,
          request.description,
          request.status
        )

    /** Equipment instance updating request to equipment instance mapping.
      * @param request
      *   Equipment instance updating request.
      * @return
      *   Equipment instance.
      */
    implicit def updateRequestToInstance(
      request: UpdateInstanceRequest
    ): EquipmentInstance =
        EquipmentInstance(
          None,
          randomUid,
          randomUid,
          request.name,
          request.description,
          request.status
        )

    /** Equipment instance to equipment instance response mapping.
      * @param instance
      *   Equipment instance.
      * @return
      *   Equipment instance response.
      */
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

    /** Equipment instance sequence to equipment instance response sequence
      * mapping.
      * @param instances
      *   Equipment instance sequence.
      * @return
      *   Equipment instance response sequence.
      */
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

    implicit def addRequestToParam(request: AddParamRequest): Param = Param(
      None,
      randomUid,
      request.modelUid,
      request.name,
      request.measurmentUnits
    )

    implicit def updateRequestToParam(request: UpdateParamRequest): Param =
        Param(
          None,
          randomUid,
          randomUid,
          request.name,
          request.measurmentUnits
        )

    implicit def paramToResponse(param: Param): ParamResponse = ParamResponse(
      param.uid,
      param.modelUid,
      param.name,
      param.measurmentUnits
    )

    implicit def paramsToResponses(params: Seq[Param]): Seq[ParamResponse] =
        for { param <- params } yield ParamResponse(
          param.uid,
          param.modelUid,
          param.name,
          param.measurmentUnits
        )
}
