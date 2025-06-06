package com.motorro.architecture.account.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.CreationExtras
import com.motorro.architecture.account.AuthenticationProviderAdapter
import com.motorro.architecture.account.eu.EUProvider
import com.motorro.architecture.appcore.account.AuthenticationProvider
import com.motorro.architecture.appcore.di.ActivityContainer

/**
 * Provides content fragment dependencies
 * @param fragment Fragment
 * @param activityContainer Parent dependency provider
 */
class CreateAccountFragmentContainerImpl(
    override val fragment: Fragment,
    private val activityContainer: ActivityContainer
): CreateAccountFragmentContainer, ActivityContainer by activityContainer {

    /**
     * Creation extras from fragment
     */
    override val defaultViewModelCreationExtras: CreationExtras get() = super<CreateAccountFragmentContainer>.defaultViewModelCreationExtras

    override val adapterFactory: ((AuthenticationProvider) -> Unit) -> AuthenticationProviderAdapter = { onClick ->
        AuthenticationProviderAdapter(
            providers = listOf(
                EUProvider
            ),
            onClick = onClick
        )
    }
}