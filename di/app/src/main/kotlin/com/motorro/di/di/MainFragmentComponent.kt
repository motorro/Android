package com.motorro.di.di

import androidx.fragment.app.Fragment
import com.motorro.di.MainFragment
import com.motorro.di.di.scopes.FragmentScoped
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScoped
@Subcomponent(modules = [MainFragmentModule::class])
interface MainFragmentComponent {
    /**
     * Explicit component factory
     */
    @Subcomponent.Factory
    interface Builder {
        fun build(@BindsInstance fragment: Fragment): MainFragmentComponent
    }

    /**
     * Injects fragment dependencies
     */
    fun inject(fragment: MainFragment)
}