package com.motorro.tasks.app.net

import com.motorro.tasks.app.data.AppError
import com.motorro.tasks.auth.data.SessionError
import com.motorro.tasks.data.ErrorCode
import com.motorro.tasks.data.HttpResponse
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.io.IOException
import java.net.UnknownServiceException

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

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    return when(response) {
        is HttpResponse.Data -> {
            Result.success(response.data)
        }
        is HttpResponse.Error -> {
            when(response.code) {
                ErrorCode.UNAUTHORIZED, ErrorCode.FORBIDDEN -> {
                    Result.failure(AppError.Authentication(SessionError.Authentication(response.code, response.message)))
                }
                ErrorCode.CONFLICT, ErrorCode.NOT_FOUND, ErrorCode.BAD_REQUEST -> {
                    Result.failure(AppError.WorkFlow(response.code, response.message))
                }
                ErrorCode.UNKNOWN -> {
                    Result.failure(AppError.Unknown(UnknownServiceException("Unknown server exception")))
                }
                else -> {
                    // If tested with a working system, consider all errors as connectivity
                    Result.failure(AppError.Network(IOException(response.message)))
                }
            }
        }
    }
}