package com.motorro.statemachine.common.data.exception

/**
 * Unknown exception
 * Always fatal as we don't know how to handle it
 */
data class UnknownException(override val message: String, override val cause: Throwable? = null) : AppException() {
    override val isFatal: Boolean get() = true
}