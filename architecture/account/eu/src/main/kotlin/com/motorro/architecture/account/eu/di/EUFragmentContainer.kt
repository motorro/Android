package com.motorro.architecture.account.eu.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.motorro.architecture.account.eu.EUViewModel
import com.motorro.architecture.appcore.di.ActivityContainer
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.appcore.di.ProvidesActivityContainer

/**
 * Creates EU fragment Container
 */
internal fun Fragment.buildEUFragmentContainer(): FragmentContainer = EUFragmentContainer(
    fragment = this,
    activityContainer = (requireActivity() as ProvidesActivityContainer).activityContainer
)

/**
 * Russian EU Container
 */
private class EUFragmentContainer(
    override val fragment: Fragment,
    private val activityContainer: ActivityContainer
) : FragmentContainer, ActivityContainer by activityContainer {
    override val defaultViewModelCreationExtras: CreationExtras get() = fragment.defaultViewModelCreationExtras

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            EUViewModel(sessionManager)
        }
    }
}