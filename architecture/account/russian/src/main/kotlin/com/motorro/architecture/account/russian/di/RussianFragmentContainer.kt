package com.motorro.architecture.account.russian.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.motorro.architecture.account.russian.RussianViewModel
import com.motorro.architecture.appcore.di.ActivityContainer
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.appcore.di.ProvidesActivityContainer

/**
 * Creates russian fragment Container
 */
internal fun Fragment.buildRussianFragmentContainer(): FragmentContainer = RussianFragmentContainer(
    fragment = this,
    activityContainer = (requireActivity() as ProvidesActivityContainer).activityContainer
)

/**
 * Russian fragment Container
 */
private class RussianFragmentContainer(
    override val fragment: Fragment,
    private val activityContainer: ActivityContainer
) : FragmentContainer, ActivityContainer by activityContainer {
    override val defaultViewModelCreationExtras: CreationExtras get() = fragment.defaultViewModelCreationExtras

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            RussianViewModel(sessionManager)
        }
    }
}