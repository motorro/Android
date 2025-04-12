package com.motorro.network.net.usecase

import com.motorro.network.data.Profile
import com.motorro.network.net.UserApi

/**
 * Use case to get a full user profile.
 */
interface GetProfile {
    /**
     * Invokes the use case to get a profile.
     *
     * @param userId The user ID to get the profile for.
     * @return A user profile.
     */
    suspend operator fun invoke(userId: Int): Result<Profile>

    class Impl(private val userApi: UserApi) : GetProfile {
        override suspend fun invoke(userId: Int): Result<Profile> {
            return userApi.getProfile(userId)
        }
    }
}