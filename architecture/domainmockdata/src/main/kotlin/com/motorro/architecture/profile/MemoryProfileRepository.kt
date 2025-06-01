package com.motorro.architecture.profile

import com.motorro.architecture.core.error.CoreException
import com.motorro.architecture.domain.profile.ProfileRepository
import com.motorro.architecture.model.user.NewUserProfile
import com.motorro.architecture.model.user.UserId
import com.motorro.architecture.model.user.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Stores profile data in memory
 */
class MemoryProfileRepository(private val delay: Long, initial: Map<UserId, UserProfile> = emptyMap(), private val error: CoreException? = null) : ProfileRepository {

    private val profiles = MutableStateFlow(initial)

    override fun getProfile(userId: UserId): Flow<UserProfile?> = profiles.map {
        delay(delay)
        if (null == error) {
            it[userId]
        } else {
            throw error
        }
    }

    override suspend fun setProfile(userId: UserId, profile: NewUserProfile) {
        delay(delay)
        if (null == error) {
            profiles.update { it + (userId to UserProfile(userId, profile.displayName, profile.countryCode)) }
        } else {
            throw error
        }
    }
}