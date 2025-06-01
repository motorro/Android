package com.motorro.architecture.content.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.CreationExtras
import com.motorro.architecture.appcore.di.ActivityContainer
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.appcore.di.ProvidesActivityContainer
import com.motorro.architecture.content.ContentViewModel
import com.motorro.architecture.country.CountryRegistry
import com.motorro.architecture.domain.di.DomainModule

/**
 * Content fragment container
 */
interface ContentFragmentContainer : FragmentContainer {
    /**
     * Country registry (on-demand creation)
     */
    val countryRegistry: CountryRegistry
}

/**
 * Container builder
 */
fun Fragment.buildContentFragmentContainer(): ContentFragmentContainer = ContentFragmentContainerImpl(
    fragment = this,
    activityContainer = (requireActivity() as ProvidesActivityContainer).activityContainer
)

/**
 * Provides content fragment dependencies
 * @param fragment Fragment
 * @param activityContainer Parent dependency provider
 */
private class ContentFragmentContainerImpl(
    override val fragment: Fragment,
    private val activityContainer: ActivityContainer
): ContentFragmentContainer, ActivityContainer by activityContainer {
    /**
     * Creation extras from fragment
     */
    override val defaultViewModelCreationExtras: CreationExtras get() = super<ContentFragmentContainer>.defaultViewModelCreationExtras

    /**
     * View-model factory (on-demand creation)
     */
    override val defaultViewModelProviderFactory: ContentViewModel.Factory get() = ContentViewModel.Factory(
        DomainModule.provideGetCurrentUserProfileUsecase(
            activityContainer.sessionManager,
            activityContainer.profileRepository
        )
    )

    /**
     * Country registry (on-demand creation)
     */
    override val countryRegistry: CountryRegistry = CountryRegistry()
}
