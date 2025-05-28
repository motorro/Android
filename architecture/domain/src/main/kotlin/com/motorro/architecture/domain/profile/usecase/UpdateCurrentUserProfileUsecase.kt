package com.motorro.architecture.domain.profile.usecase

import com.motorro.architecture.core.error.CoreException
import com.motorro.architecture.core.error.toCore
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.core.log.Logging
import com.motorro.architecture.domain.profile.ProfileRepository
import com.motorro.architecture.domain.session.SessionManager
import com.motorro.architecture.domain.session.transformUserId
import com.motorro.architecture.model.user.NewUserProfile
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Updates current user profile
 */
interface UpdateCurrentUserProfileUsecase {
    /**
     * Updates current user profile
     * @param profile Profile data to set
     * @return Flow of operation progress
     */
    operator fun invoke(profile: NewUserProfile): Flow<LceState<Unit, CoreException>>
}

/**
 * [UpdateCurrentUserProfileUsecase] implementation
 */
internal class UpdateCurrentUserProfileUsecaseImpl(
    private val sessionManager: SessionManager,
    private val repository: ProfileRepository
) : UpdateCurrentUserProfileUsecase, Logging {
    override fun invoke(profile: NewUserProfile): Flow<LceState<Unit, CoreException>> = sessionManager.transformUserId { userId ->
        emit(LceState.Loading())
        try {
            repository.setProfile(userId, profile)
            emit(LceState.Content(Unit))
        } catch (e: Throwable) {
            currentCoroutineContext().ensureActive()
            emit(LceState.Error(e.toCore()))
        }
    }.distinctUntilChanged()
}
