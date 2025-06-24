package com.motorro.di.di

import android.content.Context
import com.motorro.di.di.scopes.ApplicationScoped
import dagger.BindsInstance
import dagger.Component

/**
 * DI container
 */
@ApplicationScoped
@Component(modules = [
    GlobalScopeModule::class,
    ApplicationModule::class
])
interface ApplicationComponent {
    /**
     * Explicit component factory
     */
    @Component.Factory
    interface Factory {
        /**
         * Provides application module and builds component
         * @param context Context becomes a part of the graph
         */
        fun build(@BindsInstance context: Context): ApplicationComponent
    }

    /**
     * Main activity subcomponent builder
     */
    fun mainActivityComponentBuilder(): MainActivityComponent.Builder
}