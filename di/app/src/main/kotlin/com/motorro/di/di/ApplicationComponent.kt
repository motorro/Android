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
     * Timer
     */
    fun timer(): Timer
}