package com.motorro.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val userId: Int,
    val name: String,
    val userpic: URIString
)