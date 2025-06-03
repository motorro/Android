package com.motorro.architecture.registration.data

import com.motorro.architecture.model.user.CountryCode

/**
 * Container to store registration data across fragments
 */
interface RegistrationData {
    /**
     * User name
     */
    val name: Storage<String>

    /**
     * User name
     */
    val country: Storage<CountryCode>
}