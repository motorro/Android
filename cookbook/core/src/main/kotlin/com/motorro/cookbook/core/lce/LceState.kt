package com.motorro.cookbook.core.lce

import com.motorro.cookbook.core.error.CoreException

/**
 * Loading - Content - Error state for data loading
 * @param DATA Expected data type
 * @param ERR Expected error type
 */
sealed class LceState<out DATA: Any, out ERR: CoreException> {
    /**
     * State data
     */
    abstract val data: DATA?

    /**
     * Loading data
     * @param DATA Expected data type
     * @property data Data so far (if any)
     */
    data class Loading<out DATA: Any>(override val data: DATA? = null) : LceState<DATA, Nothing>()

    /**
     * Content
     * @param DATA Expected data type
     * @property data Loaded data
     */
    data class Content<out DATA: Any>(override val data: DATA) : LceState<DATA, Nothing>()

    /**
     * Data load error
     * @param DATA Expected data type
     * @param ERR Expected error type
     * @property error Load error
     * @property data Data so far (if any)
     */
    data class Error<out DATA: Any, out ERR : CoreException>(val error: ERR, override val data: DATA? = null) : LceState<DATA, ERR>()
}