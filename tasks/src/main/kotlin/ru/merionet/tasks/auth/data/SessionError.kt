package ru.merionet.tasks.auth.data

import ru.merionet.core.error.WithRetry
import ru.merionet.tasks.data.ErrorCode
import java.io.IOError
import java.io.IOException

/**
 * Session manager error
 */
sealed interface SessionError : WithRetry {
    /**
     * Failed to read/write the storage
     */
    class Storage(cause: Throwable): IOError(cause), SessionError {
        /**
         * No way to recover but probably to reinstall the app - no point in retrying
         */
        override val retriable: Boolean = false
    }

    /**
     * Authentication error. Credentials are invalid, etc.
     */
    class Authentication(val errorCode: ErrorCode, message: String): IllegalStateException(message), SessionError {
        /**
         * No point in retrying before user changes the input
         */
        override val retriable: Boolean = false
    }

    /**
     * Some network error occurred. Worth retrying when connection is back
     */
    class Network(cause: Throwable): IOException(cause), SessionError {
        /**
         * May be retried
         */
        override val retriable: Boolean = true
    }
}
