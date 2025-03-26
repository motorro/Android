package com.motorro.sqlite.data

/**
 * Photo filter
 */
data class PhotoFilter(val name: String? = null) {
    /**
     * Filter is empty
     */
    val isEmpty: Boolean get() = name.isNullOrBlank()
}