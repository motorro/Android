package com.motorro.architecture.registration.registering.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.motorro.architecture.appcore.di.ActivityContainer
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.appcore.di.ProvidesActivityContainer
import com.motorro.architecture.domain.di.DomainModule
import com.motorro.architecture.registration.di.RegistrationChildFragmentContainer
import com.motorro.architecture.registration.registering.RegisteringViewModel

/**
 * Creates country fragment Container
 */
internal fun Fragment.buildRegisteringFragmentContainer(): FragmentContainer = RegisteringFragmentContainer(
    fragment = this,
    activityContainer = (requireActivity() as ProvidesActivityContainer).activityContainer
)

/**
 * Country fragment Container
 */
private class RegisteringFragmentContainer(fragment: Fragment, activityContainer: ActivityContainer) : RegistrationChildFragmentContainer(fragment, activityContainer) {
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            RegisteringViewModel(
                registrationData,
                DomainModule.provideUpdateCurrentUserProfileUsecase(
                    activityContainer.sessionManager,
                    activityContainer.profileRepository
                )
            )
        }
    }
}