package ru.merionet.tasks.app.net

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import ru.merionet.tasks.app.data.AppError
import ru.merionet.tasks.auth.data.SessionError
import ru.merionet.tasks.data.ErrorCode
import ru.merionet.tasks.data.HttpResponse
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