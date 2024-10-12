package ru.merionet.tasks.auth.data

import kotlinx.serialization.Serializable
import ru.merionet.tasks.data.SessionClaims

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