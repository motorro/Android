package com.motorro.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.net.URI
import kotlin.time.Instant

@Serializable
data class Profile(
    @SerialName("id")
    val userId: Int,
    val name: String,
    val age: Int,
    val phone: Phone,
    val registered: Instant,
    @Serializable(with = URISerializer::class)
    val userpic: URI,
    val interests: Set<String> = emptySet()
)