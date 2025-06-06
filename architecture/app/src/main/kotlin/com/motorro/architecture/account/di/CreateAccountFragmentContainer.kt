package com.motorro.architecture.account.di

import androidx.fragment.app.Fragment
import com.motorro.architecture.account.AuthenticationProviderAdapter
import com.motorro.architecture.appcore.account.AuthenticationProvider
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.appcore.di.ProvidesActivityContainer

/**
 * Create account fragment Container
 */
interface CreateAccountFragmentContainer : FragmentContainer {
    /**
     * Creates provider adapter
     */
    val adapterFactory: ((AuthenticationProvider) -> Unit) -> AuthenticationProviderAdapter
}

/**
 * Container builder
 */
fun Fragment.buildContentFragmentContainer(): CreateAccountFragmentContainer = CreateAccountFragmentContainerImpl(
    fragment = this,
    activityContainer = (requireActivity() as ProvidesActivityContainer).activityContainer
)
