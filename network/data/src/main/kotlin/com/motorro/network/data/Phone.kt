package com.motorro.network.data

import kotlinx.serialization.Serializable

@Serializable
data class Phone(val countryCode: Int, val number: String) {
    override fun toString(): String {
        return "+$countryCode $number"
    }
}
