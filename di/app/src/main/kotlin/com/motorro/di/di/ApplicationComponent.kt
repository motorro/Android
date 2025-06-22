package com.motorro.di.di

import com.motorro.di.timer.Timer
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
         */
        fun build(module: ApplicationModule): ApplicationComponent
    }


    /**
     * Timer
     */
    fun timer(): Timer
}