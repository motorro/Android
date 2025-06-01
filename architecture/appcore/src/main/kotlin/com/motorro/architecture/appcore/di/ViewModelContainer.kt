package com.motorro.architecture.appcore.di

import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.reflect.KClass

/**
 * Container to provide view-model
 */
interface ViewModelContainer : ViewModelStoreOwner, HasDefaultViewModelProviderFactory

/**
 * Lazy view-model provider
 */
class ViewModelContainerLazy<VM : ViewModel> (
    private val component: () -> ViewModelContainer,
    private val viewModelClass: KClass<VM>
) : Lazy<VM> {

    private var cached: VM? = null

    override val value: VM get() {
        var viewModel = cached
        if (null != viewModel) return viewModel
        viewModel = with(component()) {
            ViewModelProvider.create(viewModelStore, defaultViewModelProviderFactory, defaultViewModelCreationExtras)[viewModelClass]
        }
        cached = viewModel
        return viewModel
    }

    override fun isInitialized(): Boolean = cached != null
}