    package com.motorro.cookbook.domain.session

import com.motorro.cookbook.model.Profile

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
