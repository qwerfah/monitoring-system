package com.qwerfah.equipment.json

import io.circe.{Decoder, HCursor}

import cats.data.Validated._
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.implicits._

import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.json.JsonSchemas._

/** Provide custom json decoders with validation for different model resources.
  */
object Decoders {
    implicit val decodeModelRequest: Decoder[ModelRequest] = (c: HCursor) => {
        modelRequestSchema.validate(c.value) match {
            case Valid(()) =>
                for {
                    name <- c.downField("name").as[String]
                    description <- c.downField("description").as[Option[String]]
                } yield ModelRequest(name, description)
            case Invalid(errors) =>
                throw InvalidJsonBodyException(errors)
        }
    }

    implicit val decodeAddInstanceRequest: Decoder[AddInstanceRequest] =
        (c: HCursor) => {
            addInstanceRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        modelUid <- c.downField("modelUid").as[Uid]
                        name <- c.downField("name").as[String]
                        description <- c
                            .downField("description")
                            .as[Option[String]]
                        status <- c.downField("status").as[EquipmentStatus]
                    } yield AddInstanceRequest(
                      modelUid,
                      name,
                      description,
                      status
                    )
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeUpdateInstanceRequest: Decoder[UpdateInstanceRequest] =
        (c: HCursor) => {
            updateInstanceRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        name <- c.downField("name").as[String]
                        description <- c
                            .downField("description")
                            .as[Option[String]]
                        status <- c.downField("status").as[EquipmentStatus]
                    } yield UpdateInstanceRequest(name, description, status)
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeAddParamRequest: Decoder[AddParamRequest] =
        (c: HCursor) => {
            addParamRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        modelUid <- c.downField("modelUid").as[Uid]
                        name <- c.downField("name").as[String]
                        measurmentUnits <- c
                            .downField("measurmentUnits")
                            .as[Option[String]]
                    } yield AddParamRequest(
                      modelUid,
                      name,
                      measurmentUnits
                    )
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeUpdateParamRequest: Decoder[UpdateParamRequest] =
        (c: HCursor) => {
            updateParamRequestSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        name <- c.downField("name").as[String]
                        measurmentUnits <- c
                            .downField("measurmentUnits")
                            .as[Option[String]]
                    } yield UpdateParamRequest(
                      name,
                      measurmentUnits
                    )
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeInstanceResponse: Decoder[InstanceResponse] =
        (c: HCursor) => {
            instanceResponseSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        uid <- c.downField("uid").as[Uid]
                        modelUid <- c.downField("modelUid").as[Uid]
                        name <- c.downField("name").as[String]
                        description <- c
                            .downField("description")
                            .as[Option[String]]
                        status <- c.downField("status").as[EquipmentStatus]
                    } yield InstanceResponse(
                      uid,
                      modelUid,
                      name,
                      description,
                      status
                    )
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }

    implicit val decodeParamResponse: Decoder[ParamResponse] =
        (c: HCursor) => {
            paramResponseSchema.validate(c.value) match {
                case Valid(()) =>
                    for {
                        uid <- c.downField("uid").as[Uid]
                        modelUid <- c.downField("modelUid").as[Uid]
                        name <- c.downField("name").as[String]
                        measurmentUnits <- c
                            .downField("measurmentUnits")
                            .as[Option[String]]
                    } yield ParamResponse(
                      uid,
                      modelUid,
                      name,
                      measurmentUnits
                    )
                case Invalid(errors) =>
                    throw InvalidJsonBodyException(errors)
            }
        }
}
