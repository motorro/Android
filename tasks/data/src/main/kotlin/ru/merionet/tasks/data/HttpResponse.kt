package ru.merionet.tasks.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Common HTTP response data
 */
@Serializable
sealed class HttpResponse<out T: Any> {
    abstract val message: String

    /**
     * Ok with data
     */
    @Serializable
    @SerialName("Data")
    data class Data<out T: Any>(val data: T, override val message: String = "OK") : HttpResponse<T>()

    /**
     * Error
     */
    @Serializable
    @SerialName("Error")
    data class Error(val code: ErrorCode, override val message: String) : HttpResponse<Nothing>()
}

enum class ErrorCode(val defaultMessage: String) {
    NOT_FOUND("NOT FOUND"),
    BAD_REQUEST("BAD REQUEST"),
    CONFLICT("CONFLICT"),
    UNAUTHORIZED("UNAUTHORIZED"),
    FORBIDDEN("FORBIDDEN"),
    UNKNOWN("UNKNOWN ERROR")
}