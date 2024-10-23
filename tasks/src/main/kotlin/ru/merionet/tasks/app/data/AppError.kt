package ru.merionet.tasks.app.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ru.merionet.core.error.WithRetry
import ru.merionet.tasks.R
import ru.merionet.tasks.auth.data.SessionError
import ru.merionet.tasks.data.ErrorCode

/**
 * Application error
 */
sealed class AppError : Exception(), WithRetry {
    /**
     * Failed to read/write the storage
     */
    class Storage(override val cause: Throwable) : AppError() {
        /**
         * No way to recover but probably to reinstall the app - no point in retrying
         */
        override val retriable: Boolean = false
    }

    /**
     * Session error. Credentials are invalid, need to re-login.
     */
    class Authentication(override val cause: SessionError) : AppError() {
        /**
         * No point in retrying before user changes the input
         */
        override val retriable: Boolean = cause.retriable
    }

    /**
     * Flow error. Session data conflict, illegal state, etc
     */
    class WorkFlow(val code: ErrorCode, override val message: String) : AppError() {
        /**
         * No point in retrying before altering the flow
         */
        override val retriable: Boolean = false
    }

    /**
     * Some network error occurred. Worth retrying when connection is back
     */
    class Network(override val cause: Throwable): AppError() {
        /**
         * May be retried
         */
        override val retriable: Boolean = true
    }

    /**
     * Unknown network. Consider critical as we don't know how to handle it
     */
    class Unknown(override val cause: Throwable): AppError() {
        /**
         * May be retried
         */
        override val retriable: Boolean = false
    }
}

/**
 * Checks the error and maps to application
 */
fun Throwable.toApp(): AppError = when(this) {
    is AppError -> this
    else -> AppError.Unknown(this)
}

/**
 * Builds localized error message
 */
fun AppError.getMessage(context: Context): String {
    val originalMessage = message ?: cause?.message
    return when (this) {
        is AppError.Authentication -> context.getString(R.string.err_authentication, originalMessage ?: context.getString(R.string.err_unknown))
        is AppError.WorkFlow -> context.getString(R.string.err_workflow, originalMessage ?: context.getString(R.string.err_unknown))
        is AppError.Network -> context.getString(R.string.err_network)
        is AppError.Storage -> context.getString(R.string.err_storage)
        is AppError.Unknown -> originalMessage ?: context.getString(R.string.err_unknown)
    }
}

/**
 * Builds localized error message
 */
@Composable
fun AppError.getMessage(): String = getMessage(LocalContext.current)
