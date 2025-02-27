package com.motorro.navigation

import android.app.Application
import com.motorro.navigation.data.SessionManager

class App : Application() {

    lateinit var sessionManager: SessionManager

    override fun onCreate() {
        super.onCreate()
        sessionManager = SessionManager.Impl(this, "session")
        Notifications.init(this)
    }
}
