package com.motorro.cookbook.app.net

import kotlinx.serialization.json.Json

/**
 * Provides JSON serialization setup
 */
fun lenientJson(): Json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    encodeDefaults = true
}