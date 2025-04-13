package com.motorro.network.net.usecase

import com.motorro.network.data.Profile
import com.motorro.network.data.User
import com.motorro.network.net.UserApi

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

    class Impl(private val userApi: UserApi) : CreateUser {
        override suspend fun invoke(profile: Profile): Result<User> {
            return userApi.createUser(profile)
        }
    }
}