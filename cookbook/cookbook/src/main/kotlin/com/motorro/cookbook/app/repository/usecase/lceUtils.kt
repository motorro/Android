package com.motorro.cookbook.app.repository.usecase

import com.motorro.core.lce.LceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

/**
 * Replaces LceState data to another type with [block]
 * If [data] is null, maps [LceState.Content] to [LceState.Loading]
 */
fun <DATA: Any, ERR: Throwable> LceState<*, ERR>.replaceData(data: DATA?): LceState<DATA, ERR> = when (this) {
    is LceState.Loading -> LceState.Loading(data)
    is LceState.Content -> data?.let { LceState.Content(it) } ?: LceState.Loading()
    is LceState.Error -> LceState.Error(error, data)
}

/**
 * Runs [block] on each valid data
 */
fun <DATA: Any, ERR: Throwable> Flow<LceState<DATA, ERR>>.onEachData(block: suspend (DATA) -> Unit): Flow<LceState<DATA, ERR>> = onEach { state ->
    state.data?.let { block(it) }
}