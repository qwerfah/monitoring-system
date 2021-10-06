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

    val userResponseSchema: Schema = Schema.load(
      json""" {
        "type": "object",
        "required": ["uid", "login", "role"],
        "properties": {
            "uid": {
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
                "enum": ["SystemAdmin", "EquipmentAdmin", "EquipmentUser"]
            },
            "token": {
                "type": "object",
                "required": ["access", "refresh"],
                "access": {
                    "type": "string",
                    "minLength": 1,
                    "pattern": "^(?!\\s*$$).+"
                },
                "refresh": {
                    "type": "string",
                    "minLength": 1,
                    "pattern": "^(?!\\s*$$).+"
                }
            }
        }  
    }"""
    )

    val tokenSchema: Schema = Schema.load(
      json""" {
        "type": "object",
        "required": ["access", "refresh"],
        "access": {
            "type": "string",
            "minLength": 1,
            "pattern": "^(?!\\s*$$).+"
        },
        "refresh": {
            "type": "string",
            "minLength": 1,
            "pattern": "^(?!\\s*$$).+"
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
