@file:OptIn(ExperimentalSerializationApi::class)

package com.motorro.network.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * User traits
 */
@Serializable
@JsonClassDiscriminator("kind")
sealed class Trait {
    /**
     * Trait name
     */
    abstract val name: String

    /**
     * Power user trait
     */
    @Serializable
    @SerialName("powerUser")
    data object PowerUser : Trait() {
        override val name: String = "Power User"
    }

    /**
     * Some badge
     */
    @Serializable
    @SerialName("badge")
    data class Badge(override val name: String) : Trait()

    /**
     * Some achievement
     */
    @Serializable
    @SerialName("achievement")
    data class Achievement(override val name: String, val score: Int) : Trait()
}