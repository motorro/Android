package com.motorro.repository.usecase

import com.motorro.core.lce.LceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * LCE loader
 *
 * @param E type of error
 */
interface LceLoader<E: Throwable> {
    /**
     * Loads data reporting progress as LCE state
     * @param soFar current data
     * @param block loading block
     */
    fun <T: Any> load(soFar: T? = null, block: suspend () -> Result<T>): Flow<LceState<T, E>>
}

/**
 * Creates a LCE loader for the given error type
 * @param convertError function to convert [Throwable] to [E]
 */
fun <E : Throwable> createLceLoader(convertError: (Throwable) -> E): LceLoader<E> = object : LceLoader<E> {
    override fun <T : Any> load(soFar: T?, block: suspend () -> Result<T>): Flow<LceState<T, E>> = flow {
        emit(LceState.Loading(soFar))
        val result = block()
        if (result.isSuccess) {
            emit(LceState.Content(result.getOrThrow()))
        } else {
            val exception = result.exceptionOrNull() ?: IllegalStateException("Unknown error")
            emit(LceState.Error(convertError(exception), soFar))
        }
    }
}