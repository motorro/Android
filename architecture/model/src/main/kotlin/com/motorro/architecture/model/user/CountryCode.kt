package com.motorro.architecture.model.user

import kotlinx.serialization.Serializable

/**
 * Alpha-3 country code
 */
@JvmInline
@Serializable
value class CountryCode(val code: String?) {
    companion object {
        /**
         * Value object for unknown country
         */
        val UNKNOWN = CountryCode(null)
    }
}