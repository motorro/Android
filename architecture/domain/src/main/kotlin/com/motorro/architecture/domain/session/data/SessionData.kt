package com.motorro.architecture.domain.session.data

import com.motorro.architecture.model.user.UserId
import kotlinx.serialization.Serializable

/**
 * Session data
 * @property accessToken Access token
 * @property refreshToken Refresh token
 * @property userId User ID
 */
@Serializable
data class SessionData(val accessToken: String, val refreshToken: String, val userId: UserId) {
    override fun toString(): String = "Session (userId: $userId)"
}