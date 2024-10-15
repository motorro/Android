package ru.merionet.tasks.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(val username: UserName, val password: String)

@Serializable
@SerialName("SessionClaims")
data class SessionClaims(val username: UserName, val token: String)
