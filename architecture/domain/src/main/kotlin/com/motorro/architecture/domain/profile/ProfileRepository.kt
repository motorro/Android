package com.motorro.architecture.domain.profile

import com.motorro.architecture.model.user.NewUserProfile
import com.motorro.architecture.model.user.UserId
import com.motorro.architecture.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * User profile repository
 */
interface ProfileRepository {
    /**
     * User profile data
     * @param userId User to get profile
     */
    fun getProfile(userId: UserId): Flow<UserProfile?>

    /**
     * Sets [profile] for current user
     * @param userId User to set profile for
     * @param profile New profile to set
     */
    suspend fun setProfile(userId: UserId, profile: NewUserProfile)
}