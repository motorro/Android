package com.motorro.network.data

import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class ProfileTest {

    private val profile = Profile(
        userId = 1,
        name = "Vasya",
        age = 25,
        interests = setOf("fishing", "coroutines", "soccer")
    )

    private val jsonProfile = """
    {
        "id": 1,
        "name": "Vasya",
        "age": 25,
        "interests": [
            "fishing",
            "coroutines",
            "soccer"
        ]
    }
    """.trimIndent()

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Test
    fun deserializes() {
        assertEquals(
            profile,
            json.decodeFromString(Profile.serializer(), jsonProfile)
        )
    }

    @Test
    fun serializes() {
        assertEquals(
            jsonProfile,
            json.encodeToString(Profile.serializer(), profile)
        )
    }
}