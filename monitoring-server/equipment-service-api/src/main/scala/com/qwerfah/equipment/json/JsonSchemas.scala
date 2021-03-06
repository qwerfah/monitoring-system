package com.qwerfah.equipment.json

import io.circe.literal._
import io.circe.schema.Schema

/** Provide json schemas for different model resources. */
object JsonSchemas {
    val modelRequestSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["name"],
                "properties": {
                    "name": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "description": {
                        "type": ["string", "null"],
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "params": {
                        "type": "array",
                        "items": {
                            "type": "object",
                            "required": ["name"],
                            "properties": {
                                "name": {
                                    "type": "string",
                                    "minLength": 1,
                                    "maxLength": 100,
                                    "pattern": "^(?!\\s*$$).+"
                                },
                                "measurmentUnits": {
                                    "type": ["string", "null"],
                                    "minLength": 1,
                                    "maxLength": 30,
                                    "pattern": "^(?!\\s*$$).+"
                                }
                            }
                        }
                        
                    }
                }
            }
            """
    )

    val addInstanceRequestSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["name", "status"],
                "properties": {
                    "name": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "description": {
                        "type": ["string", "null"],
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "status": {
                        "type": "string",
                        "enum": ["Active", "Inactive", "Decommissioned"]
                    }
                }
            }
            """
    )

    val updateInstanceRequestSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["name", "status"],
                "properties": {
                    "name": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "description": {
                        "type": ["string", "null"],
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "status": {
                        "type": "string",
                        "enum": ["Active", "Inactive", "Decommissioned"]
                    }
                }
            }
            """
    )

    val addParamRequestSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["name"],
                "properties": {
                    "name": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "measurmentUnits": {
                        "type": ["string", "null"],
                        "minLength": 1,
                        "maxLength": 30,
                        "pattern": "^(?!\\s*$$).+"
                    }
                }
            }
            """
    )

    val updateParamRequestSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["name"],
                "properties": {
                    "name": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "measurmentUnits": {
                        "type": ["string", "null"],
                        "minLength": 1,
                        "maxLength": 30,
                        "pattern": "^(?!\\s*$$).+"
                    }
                }
            }
            """
    )

    val modelResponseSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["uid", "name"],
                "properties": {
                    "uid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "name": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "description": {
                        "type": ["string", "null"],
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    }
                }
            }
            """
    )

    val instanceResponseSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["uid", "modelUid", "name","modelName", "status"],
                "properties": {
                    "uid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "modelUid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "name": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "modelName": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "description": {
                        "type": ["string", "null"],
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "status": {
                        "type": "string",
                        "enum": ["Active", "Inactive", "Decommissioned"]
                    }
                }
            }
            """
    )

    val paramResponseSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["uid", "modelUid", "name"],
                "properties": {
                    "uid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "modelUid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "name": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "measurmentUnits": {
                        "type": ["string", "null"],
                        "minLength": 1,
                        "maxLength": 30,
                        "pattern": "^(?!\\s*$$).+"
                    }
                }
            }
            """
    )
}
