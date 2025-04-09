package com.motorro.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName("id")
    val userId: Int,
    val name: String,
    val age: Int,
    val interests: Set<String> = emptySet()
)