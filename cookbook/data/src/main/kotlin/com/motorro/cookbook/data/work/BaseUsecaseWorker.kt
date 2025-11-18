package com.motorro.cookbook.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.core.log.Logging

internal abstract class BaseUsecaseWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params), Logging {
    companion object {
        const val MAX_RETRIES = 5
    }

    protected fun CoreException.toWorkResult() = when {
        isFatal -> {
            d { "Fatal error. Failed..." }
            Result.failure(toErrorData())
        }
        runAttemptCount >= MAX_RETRIES -> {
            d { "Retries exhausted. Failed..." }
            Result.failure(toErrorData())
        }
        else -> {
            d { "Retrying..." }
            Result.retry()
        }
    }
}