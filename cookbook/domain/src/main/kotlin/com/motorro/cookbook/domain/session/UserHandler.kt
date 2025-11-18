package com.motorro.cookbook.domain.session

import com.motorro.cookbook.model.Profile

/**
 * Handles user login and logout
 */
interface UserHandler {
    /**
     * Called when the user has logged in
     */
    suspend fun onLoggedIn(loggedIn: Profile)

    /**
     * Called when the user has logged out
     */
    suspend fun onLoggedOut(loggedOut: Profile)
}