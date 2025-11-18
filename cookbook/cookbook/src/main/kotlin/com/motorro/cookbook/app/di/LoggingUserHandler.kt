package com.motorro.cookbook.app.di

import com.motorro.cookbook.core.log.Logging
import com.motorro.cookbook.domain.session.UserHandler
import com.motorro.cookbook.model.Profile
import javax.inject.Inject

/**
 * Sample user handler that logs users
 */
class LoggingUserHandler @Inject constructor() : UserHandler, Logging {
    override suspend fun onLoggedIn(loggedIn: Profile) {
        i { "User logged in: ${loggedIn.name}" }
    }

    override suspend fun onLoggedOut(loggedOut: Profile) {
        i { "User logged out: ${loggedOut.name}" }
    }
}