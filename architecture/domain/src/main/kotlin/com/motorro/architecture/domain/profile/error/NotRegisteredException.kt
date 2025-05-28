package com.motorro.architecture.domain.profile.error

import com.motorro.architecture.core.error.CoreException

/**
 * User did not complete registration
 */
class NotRegisteredException : CoreException("User is not registered", isFatal = true) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString() = "Not registered"
}