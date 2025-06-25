package com.motorro.di.di

import com.motorro.di.ViewModelFragment
import com.motorro.di.di.scopes.FragmentScoped
import dagger.Subcomponent

@FragmentScoped
@Subcomponent(modules = [ViewModelFragmentModule::class])
interface ViewModelFragmentComponent {
    /**
     * Explicit component factory
     */
    @Subcomponent.Factory
    interface Builder {
        fun build(): ViewModelFragmentComponent
    }

    /**
     * Injects fragment dependencies
     */
    fun inject(fragment: ViewModelFragment)
}