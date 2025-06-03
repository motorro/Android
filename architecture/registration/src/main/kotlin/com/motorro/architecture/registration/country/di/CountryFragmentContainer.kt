package com.motorro.architecture.registration.country.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.motorro.architecture.appcore.di.ActivityContainer
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.appcore.di.ProvidesActivityContainer
import com.motorro.architecture.country.CountryRegistry
import com.motorro.architecture.registration.country.CountryViewModel
import com.motorro.architecture.registration.di.RegistrationChildFragmentContainer

/**
 * Creates country fragment Container
 */
internal fun Fragment.buildCountryFragmentContainer(): FragmentContainer = CountryFragmentContainer(
    fragment = this,
    activityContainer = (requireActivity() as ProvidesActivityContainer).activityContainer
)

/**
 * Country fragment Container
 */
private class CountryFragmentContainer(fragment: Fragment, activityContainer: ActivityContainer) : RegistrationChildFragmentContainer(fragment, activityContainer) {
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            CountryViewModel(registrationData.country, CountryRegistry().getCountries())
        }
    }
}