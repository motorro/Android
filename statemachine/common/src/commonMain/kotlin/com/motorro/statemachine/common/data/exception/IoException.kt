package com.motorro.statemachine.common.data.exception

/**
 * IO exception
 * Always non-fatal as we could retry IO operation
 */
data class IoException(override val message: String) : AppException() {
    override val isFatal: Boolean get() = false
}