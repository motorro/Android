    package com.motorro.cookbook.app.session

import com.motorro.cookbook.app.data.CookbookError
import com.motorro.cookbook.data.Profile
import com.motorro.cookbook.data.UserId

/**
 * User authentication API
 */
interface UserApi {
    /**
     * Returns the user profile if authentication is successful
     * If this method succeeds - consider authenticated
     */
    suspend fun getProfile(username: String, password: String): Profile
}

/**
 * Temporary solution to try login
 */
class MockUserApi : UserApi {
    override suspend fun getProfile(username: String, password: String): Profile {
        if (username == "user" && password == "password") {
            return Profile(UserId(1), "user")
        } else {
            throw CookbookError.Unauthorized("Invalid credentials")
        }
    }
}