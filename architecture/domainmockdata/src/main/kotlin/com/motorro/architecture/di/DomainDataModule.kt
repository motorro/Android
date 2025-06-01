package com.motorro.architecture.di

import android.content.Context
import com.motorro.architecture.core.error.CoreException
import com.motorro.architecture.domain.profile.PROFILE
import com.motorro.architecture.domain.profile.ProfileRepository
import com.motorro.architecture.domain.session.SESSION_DATA
import com.motorro.architecture.domain.session.SessionStorage
import com.motorro.architecture.domain.session.USER_ID
import com.motorro.architecture.profile.MemoryProfileRepository
import com.motorro.architecture.session.MemorySessionStorage

/**
 * Builds domain data dependencies
 */
object DomainDataModule {

    private const val DELAY = 500L
    private const val HAS_LOGIN = true
    private const val HAS_PROFILE = true
    private val PROFILE_ERROR: CoreException? = null

    /**
     * Session storage factory
     */
    @Suppress("UNUSED_PARAMETER")
    fun provideSessionStorage(context: Context): SessionStorage = if (HAS_LOGIN) {
        MemorySessionStorage(DELAY, SESSION_DATA)
    } else {
        MemorySessionStorage(DELAY)
    }

    /**
     * Profile repository factory
     */
    @Suppress("UNUSED_PARAMETER")
    fun provideProfileRepository(context: Context): ProfileRepository = if (HAS_PROFILE) {
        MemoryProfileRepository(DELAY, mapOf(USER_ID to PROFILE), PROFILE_ERROR)
    } else {
        MemoryProfileRepository(DELAY, error = PROFILE_ERROR)
    }
}