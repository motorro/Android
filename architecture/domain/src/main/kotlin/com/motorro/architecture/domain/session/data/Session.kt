package com.motorro.architecture.domain.session.data

import kotlinx.serialization.Serializable

/**
 * Stores user data
 */
@Serializable
sealed class Session {
    data object Loading : Session() {
        override fun toString(): String = "Loading session..."
    }

    @Serializable
    data class Active(val data: SessionData) : Session() {
        override fun toString(): String = "Active (userId: ${data.userId}"
    }

    @Serializable
    data object None : Session() {
        override fun toString(): String = "No session"
    }
}