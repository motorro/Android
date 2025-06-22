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
     * Explicit component builder
     */
    @Component.Builder
    interface Builder {
        /**
         * Provides application module
         */
        fun am(module: ApplicationModule): Builder

        /**
         * Builds component
         */
        fun build(): ApplicationComponent
    }


    /**
     * Timer
     */
    fun timer(): Timer
}