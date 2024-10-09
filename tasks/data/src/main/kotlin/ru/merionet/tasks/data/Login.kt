package ru.merionet.tasks.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(val username: String, val password: String)

@Serializable
@SerialName("SessionClaims")
data class SessionClaims(val username: String, val token: String)
