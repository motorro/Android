package com.motorro.di

import android.app.Application
import com.motorro.di.di.ApplicationComponent
import com.motorro.di.di.ApplicationModule
import com.motorro.di.di.DaggerApplicationComponent
import com.motorro.di.di.ProvidesApplicationComponent

class App : Application(), ProvidesApplicationComponent {

    override lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}