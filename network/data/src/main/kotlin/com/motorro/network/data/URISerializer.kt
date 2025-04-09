package com.motorro.network.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import java.net.URI

object URISerializer: KSerializer<URI> {
    override val descriptor = PrimitiveSerialDescriptor("URI", PrimitiveKind.STRING)

    override fun deserialize(decoder: kotlinx.serialization.encoding.Decoder): URI {
        return URI(decoder.decodeString())
    }

    override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: URI) {
        encoder.encodeString(value.toString())
    }
}