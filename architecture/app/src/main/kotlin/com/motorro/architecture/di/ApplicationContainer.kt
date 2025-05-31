package com.motorro.architecture.di

import android.content.Context
import com.motorro.architecture.domain.di.DomainModule
import com.motorro.architecture.domain.profile.ProfileRepository
import com.motorro.architecture.domain.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

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

    companion object {
        fun build(context: Context): ApplicationContainer = ApplicationContainerImpl(context)
    }
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

/**
 * Provides global application dependencies
 * @param context application context
 */
private class ApplicationContainerImpl(private val context: Context) : ApplicationContainer {
    /**
     * Application coroutine scope (singleton)
     */
    @OptIn(DelicateCoroutinesApi::class)
    override val globalScope: CoroutineScope = GlobalScope

    /**
     * Session manager (lazy singleton)
     */
    override val sessionManager: SessionManager by lazy {
        DomainModule.provideSessionManager(DomainDataModule.provideSessionStorage(context), globalScope)
    }

    /**
     * Profile repository (lazy singleton)
     */
    override val profileRepository: ProfileRepository by lazy {
        DomainDataModule.provideProfileRepository(context)
    }
}
