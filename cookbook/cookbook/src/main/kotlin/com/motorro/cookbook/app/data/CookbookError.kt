package com.motorro.cookbook.app.data

/**
 * Application errors
 */
sealed class CookbookError : Throwable() {
    /**
     * Need to login
     */
    data class Unauthorized(override val message: String) : CookbookError()

    /**
     * All other errors
     */
    data class Unknown(override val cause: Throwable) : CookbookError() {
        override val message: String = cause.message ?: "Unknown error"
    }
}