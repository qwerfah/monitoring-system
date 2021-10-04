package com.qwerfah.documentation.json

import io.circe.literal._
import io.circe.schema.Schema

/** Provide json schemas for different model resources. */
object JsonSchemas {
    val fileMetaRequestSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["uid", "modelUid", "filename", "contentType"],
                "properties": {
                    "uid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "modelUid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "filename": {
                        "type": ["string", "null"],
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "contentType": {
                        "type": ["string", "null"],
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    }
                }
            }
            """
    )
}
