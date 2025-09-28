package com.motorro.cookbook.core.lce

import com.motorro.cookbook.core.error.CoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

/**
 * Replaces LceState data to another type with [data]
 * If [data] is null, maps [LceState.Content] to [LceState.Loading]
 */
fun <DATA: Any, ERR: CoreException> LceState<*, ERR>.replaceData(data: DATA?): LceState<DATA, ERR> = when (this) {
    is LceState.Loading -> LceState.Loading(data)
    is LceState.Content -> data?.let { LceState.Content(it) } ?: LceState.Loading()
    is LceState.Error -> LceState.Error(error, data)
}

/**
 * Replaces LceState empty data to another with [data]
 * If [data] is null, maps [LceState.Content] to [LceState.Loading]
 */
fun <DATA: Any, ERR: CoreException> LceState<DATA, ERR>.replaceEmptyData(data: DATA?): LceState<DATA, ERR> {
    if (null == data) return this
    return when (this) {
        is LceState.Loading -> LceState.Loading(this.data ?: data)
        is LceState.Content -> this
        is LceState.Error -> LceState.Error(error, this.data ?: data)
    }
}

/**
 * Maps LceState data to another with [block]
 * If [data] is null, maps [LceState.Content] to [LceState.Loading]
 */
fun <DATA_1: Any, DATA_2: Any, ERR: CoreException> LceState<DATA_1, ERR>.map(block: (DATA_1) -> DATA_2): LceState<DATA_2, ERR> {
    val newData = data?.let(block)

    return when(this) {
        is LceState.Loading -> LceState.Loading(newData)
        is LceState.Content -> if (null != newData) LceState.Content(newData) else LceState.Loading()
        is LceState.Error -> LceState.Error(error, newData)
    }
}

/**
 * Runs [block] on each valid data
 */
fun <DATA: Any, ERR: CoreException> Flow<LceState<DATA, ERR>>.onEachData(block: suspend (DATA) -> Unit): Flow<LceState<DATA, ERR>> = onEach { state ->
    state.data?.let { block(it) }
}