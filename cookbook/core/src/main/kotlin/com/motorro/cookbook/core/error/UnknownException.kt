package com.motorro.cookbook.core.error

/**
 * Unknown error
 */
class UnknownException(cause: Throwable, isFatal: Boolean = true) : CoreException(cause.message ?: "Unknown error", isFatal, cause) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnknownException

        return cause == other.cause
    }

    override fun hashCode(): Int {
        return cause.hashCode()
    }

    override fun toString(): String = "Unknown error: $cause"
}