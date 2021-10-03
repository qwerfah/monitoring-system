package com.qwerfah.common.json

import io.circe.literal._
import io.circe.schema.Schema

object JsonSchemas {
    val userRequestSchema: Schema = Schema.load(
      json""" {
        "type": "object",
        "required": ["login", "password", "role"],
        "properties": {
            "login": {
                "type": "string",
                "minLength": 1,
                "maxLength": 100,
                "pattern": "^(?!\\s*$$).+"
            },
            "password": {
                "type": "string",
                "minLength": 1,
                "maxLength": 100,
                "pattern": "^(?!\\s*$$).+"
            },
            "role": {
                "type": "string",
                "enum": ["SystemAdmin", "EquipmentAdmin", "EquipmentUser"]
            }
        }  
    }"""
    )

    val credentialsSchema: Schema = Schema.load(
      json""" {
        "type": "object",
        "required": ["login", "password"],
        "properties": {
            "login": {
                "type": "string",
                "minLength": 1,
                "maxLength": 100,
                "pattern": "^(?!\\s*$$).+"
            },
            "password": {
                "type": "string",
                "minLength": 1,
                "maxLength": 100,
                "pattern": "^(?!\\s*$$).+"
            }
        }  
    }"""
    )

    val payloadSchema: Schema = Schema.load(
      json""" {
        "type": "object",
        "required": ["uid", "login", "role"],
        "properties": {
            "instanceUid": {
                "type": "string",
                "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
            },
            "login": {
                "type": "string",
                "minLength": 1,
                "maxLength": 100,
                "pattern": "^(?!\\s*$$).+"
            },
            "role": {
                "type": "string",
                "enum": ["SystemAdmin", "EquipmentAdmin", "EquipmentUser", "Service"]
            }
        }  
    }"""
    )
}
