package com.motorro.architecture.registration.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.CreationExtras
import com.motorro.architecture.appcore.di.ActivityContainer
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.registration.data.RegistrationData

/**
 * Common registration sub-fragment Container
 * Takes saved-state-handle fro the parent Container
 */
internal abstract class RegistrationChildFragmentContainer(
    final override val fragment: Fragment,
    activityContainer: ActivityContainer
) : FragmentContainer, ActivityContainer by activityContainer {

    /**
     * Provides registration data
     * Stores them in parent fragment so the instance is the same for
     * all the screens
     */
    val registrationData: RegistrationData by fragment.requireParentFragment().requireParentFragment().viewModels<RegistrationDataViewModel> {
        RegistrationDataViewModel.Factory
    }

    /**
     * Sub-fragments require some common scope that has longer lifespan
     * then their own to store data across wizard flow.
     * Here we change the saved state registry owner to the parent fragment
     * which would be the navigation fragment inside the main `RegistrationFragment`
     */
    override val defaultViewModelCreationExtras: CreationExtras = fragment.defaultViewModelCreationExtras
}