package com.motorro.cookbook.data.work

import androidx.work.Data
import androidx.work.WorkInfo
import androidx.work.workDataOf
import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.session.error.UnauthorizedException

private const val PARAM_ERROR_CLASS = "errorClass"
private const val PARAM_ERROR_MESSAGE = "errorMessage"
private const val PARAM_ERROR_IS_FATAL = "errorIsFatal"

/**
 * Combined work status to use with LCE
 */
sealed class WorkState {
    data object Loading : WorkState()
    data class Error(val className: String, val message: String, val isFatal: Boolean) : WorkState()
    data object Idle : WorkState()
}

/**
 * Saves core exception to work data
 */
fun CoreException.toErrorData() = workDataOf(
    PARAM_ERROR_CLASS to this::class.simpleName,
    PARAM_ERROR_IS_FATAL to isFatal,
    PARAM_ERROR_MESSAGE to message
)

/**
 * Get combined work state by tag
 */
fun List<WorkInfo>.getCombinedWorkInfo(): WorkState {
    return when {
        any { WorkInfo.State.RUNNING == it.state } -> WorkState.Loading
        any { WorkInfo.State.FAILED == it.state } -> {
            val data = firstOrNull { WorkInfo.State.FAILED == it.state }?.outputData
            WorkState.Error(data.errorClassName(), data.errorMessage(), data.isFatalError())
        }
        else -> WorkState.Idle
    }
}

/**
 * Convert [WorkState] to [LceState]
 */
fun <DATA: Any> WorkState.toLce(data: DATA?): LceState<DATA, CoreException> = when(this) {
    WorkState.Idle -> if (null != data) LceState.Content(data) else LceState.Loading()
    WorkState.Loading -> LceState.Loading(data)
    is WorkState.Error -> LceState.Error(
        when(className) {
            UnauthorizedException::class.simpleName -> UnauthorizedException()
            else -> CoreException(message, isFatal)
        }
    )
}

private fun Data?.errorClassName(): String = this?.getString(PARAM_ERROR_CLASS) ?: requireNotNull(UnknownError::class.simpleName)
private fun Data?.isFatalError(): Boolean = true == this?.getBoolean(PARAM_ERROR_IS_FATAL, true)
private fun Data?.errorMessage(): String = this?.getString(PARAM_ERROR_MESSAGE) ?: "Unknown error"

