package com.motorro.architecture.profile

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
class MemoryProfileRepository(private val delay: Long, initial: Map<UserId, UserProfile> = emptyMap()) : ProfileRepository {

    private val profiles = MutableStateFlow(initial)

    override fun getProfile(userId: UserId): Flow<UserProfile?> = profiles.map {
        delay(delay)
        it[userId]
    }

    override suspend fun setProfile(userId: UserId, profile: NewUserProfile) {
        delay(delay)
        profiles.update { it + (userId to UserProfile(userId, profile.displayName, profile.countryCode)) }
    }
}