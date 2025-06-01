package com.motorro.architecture.appcore.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

/**
 * Common activity container
 */
interface ActivityContainer : ApplicationContainer, ViewModelContainer {
    /**
     * App-compat activity
     */
    val activity: AppCompatActivity

    /**
     * Default view-model store
     */
    override val viewModelStore get() = activity.viewModelStore

    /**
     * Default view-model creation extras
     */
    override val defaultViewModelCreationExtras get() = activity.defaultViewModelCreationExtras
}

/**
 * Provides activity container
 */
interface ProvidesActivityContainer {
    /**
     * Activity container
     */
    val activityContainer: ActivityContainer
}

/**
 * Creates view-model using the container
 */
inline fun <reified VM : ViewModel> ProvidesActivityContainer.containerModel(): Lazy<VM> = ViewModelContainerLazy(
    component = { this.activityContainer },
    viewModelClass = VM::class
)
