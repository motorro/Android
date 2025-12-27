package com.motorro.tasks.login

import com.motorro.tasks.domain.data.User

/**
 * Flow result interaction
 */
interface LoginFlowHost {
    /**
     * When user authenticated
     * @param user Logged-in user
     */
    fun onAuthenticated(user: User)

    /**
     * When user failed or refused to authenticate
     */
    fun onNotAuthenticated()
}