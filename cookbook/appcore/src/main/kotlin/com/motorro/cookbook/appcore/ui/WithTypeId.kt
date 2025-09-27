package com.motorro.cookbook.appcore.ui

/**
 * Represents an object that has a unique type ID.
 */
interface WithTypeId {
    /**
     * Unique type ID.
     */
    val typeId: String get() = javaClass.name
}