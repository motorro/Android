package com.motorro.sqlite.data

/**
 * Photo filter
 */
data class PhotoFilter(val name: String? = null, val tags: Set<Int> = emptySet()) {
    /**
     * Filter is empty
     */
    val isEmpty: Boolean get() = name.isNullOrBlank() && tags.isEmpty()
}