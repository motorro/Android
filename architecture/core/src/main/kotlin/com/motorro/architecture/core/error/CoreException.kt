package com.motorro.architecture.core.error

/**
 * Core application exception
 * @property message Original error message
 * @property isFatal If true - no point in retrying current operation
 * @property cause Source error if any
 */
open class CoreException(
    override val message: String,
    val isFatal: Boolean = true,
    cause: Throwable? = null
) : RuntimeException(cause)

/**
 * Checks if error is fatal
 * - If error is [CoreException] - checks explicitly
 * - Checks cause
 * - If no cause is found - considers fatal as the error is unexpected
 */
fun Throwable.isFatal(): Boolean {
    val cause = this.cause
    return when {
        this is CoreException && isFatal -> true
        null != cause -> cause.isFatal()
        else -> true
    }
}

/**
 * Transforms exception to core system
 */
fun Throwable.toCore(): CoreException = when {
    this is CoreException -> this
    else -> UnknownException(this)
}