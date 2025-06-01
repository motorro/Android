package com.motorro.architecture.appcore.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

/**
 * Common fragment container
 */
interface FragmentContainer : ActivityContainer {
    /**
     * App-compat activity
     */
    val fragment: Fragment

    /**
     * Default view-model creation extras
     */
    override val defaultViewModelCreationExtras get() = fragment.defaultViewModelCreationExtras
}

/**
 * Provides fragment container
 */
interface ProvidesFragmentContainer {
    /**
     * Fragment container
     */
    val fragmentContainer: FragmentContainer
}

/**
 * Creates view-model using the container
 */
inline fun <reified VM : ViewModel> ProvidesFragmentContainer.containerModel(): Lazy<VM> = ViewModelContainerLazy(
    component = { this.fragmentContainer },
    viewModelClass = VM::class
)

