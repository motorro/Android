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

/**
 * Checks we have some meaningful error or something that we don't know
 * how to handle
 */
fun Throwable.toCookbookError(): CookbookError = when(this) {
    is CookbookError -> this
    else -> CookbookError.Unknown(this)
}