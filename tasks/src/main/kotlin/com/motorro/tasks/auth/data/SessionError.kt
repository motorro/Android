package com.motorro.tasks.auth.data

import com.motorro.core.error.WithRetry
import com.motorro.tasks.data.ErrorCode

/**
 * Session manager error
 */
sealed class SessionError : Exception(), WithRetry {
    /**
     * Failed to read/write the storage
     */
    class Storage(override val cause: Throwable) : SessionError() {
        /**
         * No way to recover but probably to reinstall the app - no point in retrying
         */
        override val retriable: Boolean = false
    }

    /**
     * Authentication error. Credentials are invalid, etc.
     */
    class Authentication(val errorCode: ErrorCode, override val message: String) : SessionError() {
        /**
         * No point in retrying before user changes the input
         */
        override val retriable: Boolean = false
    }

    /**
     * Some network error occurred. Worth retrying when connection is back
     */
    class Network(override val cause: Throwable): SessionError() {
        /**
         * May be retried
         */
        override val retriable: Boolean = true
    }
}
