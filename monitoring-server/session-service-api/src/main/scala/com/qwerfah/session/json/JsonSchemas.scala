package com.qwerfah.session.json

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
}
