package com.motorro.architecture.appcore.di

import com.motorro.architecture.domain.profile.ProfileRepository
import com.motorro.architecture.domain.session.SessionManager
import kotlinx.coroutines.CoroutineScope

/**
 * Application container
 */
interface ApplicationContainer {
    /**
     * Application coroutine scope (singleton)
     */
    val globalScope: CoroutineScope

    /**
     * Session manager (lazy singleton)
     */
    val sessionManager: SessionManager

    /**
     * Profile repository (lazy singleton)
     */
    val profileRepository: ProfileRepository
}

/**
 * Provides application container
 */
interface ProvidesApplicationContainer {
    /**
     * Application container
     */
    val applicationContainer: ApplicationContainer
}
