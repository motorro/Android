package com.motorro.architecture

import android.app.Application
import com.motorro.architecture.core.log.Logger
import com.motorro.architecture.di.ApplicationContainer
import com.motorro.architecture.di.ProvidesApplicationContainer
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class App : Application(), ProvidesApplicationContainer {
    /**
     * Application dependencies live as long as application lives
     */
    override lateinit var applicationContainer: ApplicationContainer

    override fun onCreate() {
        super.onCreate()
        initializeLogger()
        applicationContainer = ApplicationContainer.build(this)
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