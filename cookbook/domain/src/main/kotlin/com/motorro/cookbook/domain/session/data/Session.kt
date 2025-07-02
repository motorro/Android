package com.motorro.cookbook.domain.session.data

import com.motorro.cookbook.model.Profile
import kotlinx.serialization.Serializable

/**
 * Stores user data
 */
@Serializable
sealed class Session {
    data object Loading : Session()

    @Serializable
    data class Active(val username: String, val password: String, val profile: Profile) : Session()

    @Serializable
    data object None : Session()
}