package com.motorro.architecture.domain.di

import com.motorro.architecture.domain.profile.ProfileRepository
import com.motorro.architecture.domain.profile.usecase.GetCurrentUserProfileUsecase
import com.motorro.architecture.domain.profile.usecase.GetCurrentUserProfileUsecaseImpl
import com.motorro.architecture.domain.profile.usecase.UpdateCurrentUserProfileUsecase
import com.motorro.architecture.domain.profile.usecase.UpdateCurrentUserProfileUsecaseImpl
import com.motorro.architecture.domain.session.SessionManager
import com.motorro.architecture.domain.session.SessionManagerImpl
import com.motorro.architecture.domain.session.SessionStorage
import kotlinx.coroutines.CoroutineScope

/**
 * Builds domain dependencies
 */
object DomainModule {
    /**
     * Session manager factory
     */
    fun provideSessionManager(
        sessionStorage: SessionStorage,
        scope: CoroutineScope
    ): SessionManager = SessionManagerImpl(
        sessionStorage,
        scope
    )

    /**
     * Get profile usecase factory
     */
    fun provideGetCurrentUserProfileUsecase(
        sessionManager: SessionManager,
        repository: ProfileRepository
    ): GetCurrentUserProfileUsecase = GetCurrentUserProfileUsecaseImpl(
        sessionManager,
        repository
    )

    /**
     * Update profile usecase factory
     */
    fun provideUpdateCurrentUserProfileUsecase(
        sessionManager: SessionManager,
        repository: ProfileRepository
    ): UpdateCurrentUserProfileUsecase = UpdateCurrentUserProfileUsecaseImpl(
        sessionManager,
        repository
    )
}