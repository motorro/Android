package com.motorro.di.di

import android.content.Context
import com.motorro.di.timer.Timer
import dagger.BindsInstance
import dagger.Component

/**
 * DI container
 */
@Component(modules = [
    GlobalScopeModule::class,
    ApplicationModule::class
])
interface ApplicationComponent {
    /**
     * Explicit component factory
     */
    @Component.Factory
    interface Builder {
        /**
         * Provides application module and builds component
         * @param context Context becomes a part of the graph
         */
        fun build(@BindsInstance context: Context): ApplicationComponent
    }


    /**
     * Timer
     */
    fun timer(): Timer
}