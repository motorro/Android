package com.motorro.architecture.main.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.motorro.architecture.appcore.di.ActivityContainer
import com.motorro.architecture.appcore.di.ApplicationContainer
import com.motorro.architecture.appcore.di.ProvidesApplicationContainer
import com.motorro.architecture.domain.di.DomainModule
import com.motorro.architecture.main.MainViewModel

/**
 * Container builder
 */
fun AppCompatActivity.buildMainActivityContainer(): ActivityContainer = MainActivityContainerImpl(
    activity = this,
    applicationContainer = (application as ProvidesApplicationContainer).applicationContainer
)

/**
 * Provides main activity dependencies
 * @param activity Activity
 * @param applicationContainer Common application dependency provider
 */
private class MainActivityContainerImpl(
    override val activity: AppCompatActivity,
    private val applicationContainer: ApplicationContainer
) : ActivityContainer, ApplicationContainer by applicationContainer {

    /**
     * Provides view-model
     */
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory get() = MainViewModel.Factory(
        DomainModule.provideGetCurrentUserProfileUsecase(
            applicationContainer.sessionManager,
            applicationContainer.profileRepository
        )
    )
}

