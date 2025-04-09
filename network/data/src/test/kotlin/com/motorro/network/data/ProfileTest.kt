package com.motorro.network.data

import kotlinx.serialization.json.Json
import org.junit.Test
import java.net.URI
import kotlin.test.assertEquals
import kotlin.time.Instant

class ProfileTest {

    private val profile = Profile(
        userId = 1,
        name = "Vasya",
        age = 25,
        phone = Phone(7, "1234567890"),
        registered = Instant.parse("2023-11-17T11:43:22.306Z"),
        userpic = URI("https://example.com/picture.jpg"),
        interests = setOf("fishing", "coroutines", "soccer")
    )

    private val jsonProfile = """
    {
        "id": 1,
        "name": "Vasya",
        "age": 25,
        "phone": {
            "countryCode": 7,
            "number": "1234567890"
        },
        "registered": "2023-11-17T11:43:22.306Z",
        "userpic": "https://example.com/picture.jpg",
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