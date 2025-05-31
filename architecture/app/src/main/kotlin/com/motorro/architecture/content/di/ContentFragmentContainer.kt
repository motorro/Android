package com.motorro.architecture.content.di

import com.motorro.architecture.content.ContentViewModel
import com.motorro.architecture.country.CountryRegistry
import com.motorro.architecture.domain.di.DomainModule
import com.motorro.architecture.main.di.MainActivityContainer

/**
 * Provides main activity dependencies
 * @param activityContainer Parent dependency provider
 */
class ContentFragmentContainer(private val activityContainer: MainActivityContainer) {
    /**
     * View-model factory (on-demand creation)
     */
    val contentFragmentModelFactory: ContentViewModel.Factory get() = ContentViewModel.Factory(
        DomainModule.provideGetCurrentUserProfileUsecase(
            activityContainer.sessionManager,
            activityContainer.profileRepository
        )
    )

    /**
     * Country registry (on-demand creation)
     */
    val countryRegistry: CountryRegistry = CountryRegistry()
}
