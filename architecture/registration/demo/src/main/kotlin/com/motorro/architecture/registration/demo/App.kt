package com.motorro.architecture.registration.demo

import android.app.Application
import com.motorro.architecture.appcore.di.ApplicationContainer
import com.motorro.architecture.appcore.di.ProvidesApplicationContainer
import com.motorro.architecture.core.log.Logger
import com.motorro.architecture.registration.demo.di.buildApplicationContainer
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class App : Application(), ProvidesApplicationContainer {

    override lateinit var applicationContainer: ApplicationContainer

    override fun onCreate() {
        super.onCreate()
        initializeLogger()
        applicationContainer = buildApplicationContainer(this)
    }

    private fun initializeLogger() {
        Napier.base(DebugAntilog())
        Logger.setLogger { tag, level, throwable, message ->
            Napier.log(
                priority = when(level) {
                    com.motorro.architecture.core.log.LogLevel.DEBUG -> io.github.aakira.napier.LogLevel.DEBUG
                    com.motorro.architecture.core.log.LogLevel.INFO -> io.github.aakira.napier.LogLevel.INFO
                    com.motorro.architecture.core.log.LogLevel.WARN -> io.github.aakira.napier.LogLevel.WARNING
                    com.motorro.architecture.core.log.LogLevel.ERROR -> io.github.aakira.napier.LogLevel.ERROR
                },
                tag = tag,
                throwable = throwable,
                message = message()
            )
        }
    }
}