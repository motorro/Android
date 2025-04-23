package com.motorro.repository.net

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

/**
 * Runs suspend function and returns result checking for cancellation
 */
suspend inline fun <T> coRunCatching(block: () -> T): Result<T> = try {
    Result.success(block())
} catch (e: Throwable) {
    currentCoroutineContext().ensureActive()
    Result.failure(e)
}