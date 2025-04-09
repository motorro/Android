@file:UseSerializers(URISerializer::class)

package com.motorro.network.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.Json
import org.junit.Test
import java.net.URI
import kotlin.test.assertEquals


class URISerializerTest {
    private val uri1 = URI("https://example.com")
    private val uri2 = URI("https://motorro.com")

    @Serializable
    data class UriContainer(
        val uri1: URI,
        val uri2: URI
    )

    private val serializedContainer = """{"uri1":"https://example.com","uri2":"https://motorro.com"}"""

    @Test
    fun serializes() {
        assertEquals(
            serializedContainer,
            Json.encodeToString(UriContainer.serializer(), UriContainer(uri1, uri2))
        )
    }

    @Test
    fun deserializes() {
        assertEquals(
            uri1,
            Json.decodeFromString(UriContainer.serializer(), serializedContainer).uri1
        )
        assertEquals(
            uri2,
            Json.decodeFromString(UriContainer.serializer(), serializedContainer).uri2
        )
    }
}