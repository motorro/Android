package com.motorro.architecture.domain.session.error

import com.motorro.architecture.core.error.CoreException

/**
 * Unauthorized exception
 */
class UnauthorizedException(override val cause: Throwable? = null) : CoreException("Unauthorized", isFatal = true, cause) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnauthorizedException

        return cause == other.cause
    }

    override fun hashCode(): Int {
        return cause?.hashCode() ?: 0
    }

    override fun toString() = "Unauthorized"
}