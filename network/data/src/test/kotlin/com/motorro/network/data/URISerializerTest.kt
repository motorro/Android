package com.motorro.network.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Test
import java.net.URI
import kotlin.test.assertEquals


class URISerializerTest {
    private val uri = URI("https://example.com")

    @Serializable
    data class UriContainer(
        @Serializable(with = URISerializer::class)
        val uri: URI
    )

    private val serializedContainer = """{"uri":"https://example.com"}"""

    @Test
    fun serializes() {
        assertEquals(
            serializedContainer,
            Json.encodeToString(UriContainer.serializer(), UriContainer(uri))
        )
    }

    @Test
    fun deserializes() {
        assertEquals(
            uri,
            Json.decodeFromString(UriContainer.serializer(), serializedContainer).uri
        )
    }
}