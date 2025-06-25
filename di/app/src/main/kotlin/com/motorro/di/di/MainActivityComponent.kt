package com.motorro.di.di

import androidx.appcompat.app.AppCompatActivity
import com.motorro.di.di.scopes.ActivityScoped
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent {
    /**
     * Explicit component factory
     */
    @Subcomponent.Factory
    interface Builder {
        fun build(@BindsInstance activity: AppCompatActivity): MainActivityComponent
    }

    /**
     * Main fragment component builder
     */
    fun mainFragmentComponentBuilder(): MainFragmentComponent.Builder

    /**
     * View-model fragment component builder
     */
    fun vmFragmentComponentBuilder(): ViewModelFragmentComponent.Builder
}