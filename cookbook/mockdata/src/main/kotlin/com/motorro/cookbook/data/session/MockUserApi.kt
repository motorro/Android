package com.motorro.cookbook.data.session

import com.motorro.cookbook.domain.session.UserApi
import com.motorro.cookbook.domain.session.error.UnauthorizedException
import com.motorro.cookbook.model.Profile
import com.motorro.cookbook.model.UserId
import kotlinx.coroutines.delay

/**
 * Temporary solution to try login
 */
class MockUserApi : UserApi {
    override suspend fun getProfile(username: String, password: String): Profile {
        delay(DELAY)
        if (username == "user" && password == "password") {
            return Profile(UserId(1), "user")
        } else {
            throw UnauthorizedException()
        }
    }

    companion object {
        private const val DELAY = 2000L
    }
}