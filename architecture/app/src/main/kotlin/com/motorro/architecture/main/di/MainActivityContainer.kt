package com.motorro.architecture.main.di

import com.motorro.architecture.di.ApplicationContainer
import com.motorro.architecture.domain.di.DomainModule
import com.motorro.architecture.main.MainViewModel

interface MainActivityContainer : ApplicationContainer{
    /**
     * View-model factory (on-demand creation)
     */
    val mainViewModelFactory: MainViewModel.Factory

    companion object {
        fun build(applicationContainer: ApplicationContainer): MainActivityContainer = MainActivityContainerImpl(applicationContainer)
    }
}

/**
 * Provides main activity component
 */
interface ProvidesMainActivityContainer {
    /**
     * Application container
     */
    val activityContainer: MainActivityContainer
}

/**
 * Provides main activity dependencies
 * @param applicationContainer Common application dependency provider
 */
private class MainActivityContainerImpl(private val applicationContainer: ApplicationContainer) : MainActivityContainer, ApplicationContainer by applicationContainer {
    /**
     * View-model factory (on-demand creation)
     */
    override val mainViewModelFactory: MainViewModel.Factory get() = MainViewModel.Factory(
        DomainModule.provideGetCurrentUserProfileUsecase(
            applicationContainer.sessionManager,
            applicationContainer.profileRepository
        )
    )
}

