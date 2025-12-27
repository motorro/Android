package com.motorro.tasks.auth.data

import com.motorro.tasks.data.SessionClaims
import kotlinx.serialization.Serializable

/**
 * Session state
 */
@Serializable
sealed class Session {
    /**
     * Not authenticated
     */
    @Serializable
    data object NONE : Session()

    /**
     * Active session - user logged in
     */
    @Serializable
    data class Active(val claims: SessionClaims) : Session()
}