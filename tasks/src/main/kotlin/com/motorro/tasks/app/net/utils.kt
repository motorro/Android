package com.motorro.tasks.app.net

import com.motorro.tasks.app.data.AppError
import com.motorro.tasks.auth.data.SessionError
import com.motorro.tasks.data.ErrorCode
import com.motorro.tasks.data.HttpResponse
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.io.IOException

/**
 * Runs network `block` and returns data on success
 * @throws AppError on failure
 */
suspend inline fun <R: Any> net(block: () -> HttpResponse<R>): R = netResult(block).getOrThrow()

/**
 * Runs network `block` and returns data on success
 * @throws AppError on failure
 */
suspend inline fun <R: Any> netResult(block: () -> HttpResponse<R>): Result<R> {
    val response = try {
        block()
    } catch (e: Throwable) {
        currentCoroutineContext().ensureActive()
        return Result.failure(AppError.Network(e))
    }

    when(response) {
        is HttpResponse.Data -> {
            return Result.success(response.data)
        }
        is HttpResponse.Error -> {
            when(response.code) {
                ErrorCode.UNAUTHORIZED, ErrorCode.FORBIDDEN -> {
                    return Result.failure(AppError.Authentication(SessionError.Authentication(response.code, response.message)))
                }
                else -> {
                    // If tested with a working system, consider all errors as connectivity
                    return Result.failure(AppError.Network(IOException(response.message)))
                }
            }
        }
    }
}