package com.motorro.architecture.domain.profile.usecase

import com.motorro.architecture.core.error.CoreException
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.core.log.Logging
import com.motorro.architecture.domain.profile.ProfileRepository
import com.motorro.architecture.domain.profile.error.NotRegisteredException
import com.motorro.architecture.domain.session.SessionManager
import com.motorro.architecture.domain.session.transformUserId
import com.motorro.architecture.model.user.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Retrieves user profile
 */
interface GetCurrentUserProfileUsecase {
    /**
     * Retrieves user profile
     */
    operator fun invoke(): Flow<LceState<UserProfile, CoreException>>
}


/**
 * [GetCurrentUserProfileUsecase] implementation
 */
internal class GetCurrentUserProfileUsecaseImpl(
    private val sessionManager: SessionManager,
    private val repository: ProfileRepository
) : GetCurrentUserProfileUsecase, Logging {
    override fun invoke(): Flow<LceState<UserProfile, CoreException>> = sessionManager.transformUserId { userId ->
        emit(LceState.Loading())
        repository.getProfile(userId).collect { profile ->
            if (null != profile) {
                emit(LceState.Content(profile))
            } else {
                emit(LceState.Error(NotRegisteredException()))
            }
        }
    }.distinctUntilChanged()
}
