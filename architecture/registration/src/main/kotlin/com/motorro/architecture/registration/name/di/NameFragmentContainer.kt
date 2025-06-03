package com.motorro.architecture.registration.name.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.motorro.architecture.appcore.di.ActivityContainer
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.appcore.di.ProvidesActivityContainer
import com.motorro.architecture.registration.Constants
import com.motorro.architecture.registration.di.RegistrationChildFragmentContainer
import com.motorro.architecture.registration.name.NameViewModel

/**
 * Creates name fragment Container
 */
fun Fragment.buildNameFragmentContainer(): FragmentContainer = NameFragmentContainer(
    fragment = this,
    activityContainer = (requireActivity() as ProvidesActivityContainer).activityContainer
)

/**
 * Name fragment Container
 */
private class NameFragmentContainer(fragment: Fragment, activityContainer: ActivityContainer) : RegistrationChildFragmentContainer(fragment, activityContainer) {
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            NameViewModel(registrationData.name, Constants.MIN_NAME_LENGTH)
        }
    }
}