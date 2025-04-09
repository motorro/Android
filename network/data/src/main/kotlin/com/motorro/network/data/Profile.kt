package com.motorro.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Profile(
    @SerialName("id")
    val userId: Int,
    val name: String,
    val age: Int,
    val phone: Phone,
    val registered: Instant,
    val userpic: URIString,
    val interests: Set<String> = emptySet()
)