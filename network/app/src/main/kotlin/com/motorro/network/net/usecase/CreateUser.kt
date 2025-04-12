package com.motorro.network.net.usecase

import com.motorro.network.data.Profile
import com.motorro.network.data.User
import com.motorro.network.net.UserApi
import com.motorro.network.session.SessionManager

/**
 * Create User use case
 */
interface CreateUser {
    /**
     * Creates a user
     * @param profile User profile
     * @return Created user
     */
    suspend operator fun invoke(profile: Profile): Result<User>

    class Impl(private val userApi: UserApi, private val sessionManager: SessionManager) : CreateUser {
        override suspend fun invoke(profile: Profile): Result<User> {
            return userApi.createUser(profile, "Bearer ${sessionManager.token.value}")
        }
    }
}