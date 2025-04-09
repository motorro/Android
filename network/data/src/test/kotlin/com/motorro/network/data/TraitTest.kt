package com.motorro.network.data

import junit.framework.TestCase.assertEquals
import kotlinx.serialization.json.Json
import org.junit.Test


class TraitTest {
    private val powerUser = Trait.PowerUser
    private val badge = Trait.Badge("Some badge")
    private val achievement = Trait.Achievement("Some achievement", 100)

    private val serializedPowerUser = """{"kind":"powerUser"}"""
    private val serializedBadge = """{"kind":"badge","name":"Some badge"}"""
    private val serializedAchievement = """{"kind":"achievement","name":"Some achievement","score":100}"""

    @Test
    fun serializesPowerUser() {
        assertEquals(
            serializedPowerUser,
            Json.encodeToString(Trait.serializer(), powerUser)
        )
    }

    @Test
    fun deserializesPowerUser() {
        assertEquals(
            powerUser,
            Json.decodeFromString(Trait.serializer(), serializedPowerUser)
        )
    }

    @Test
    fun serializesBadge() {
        assertEquals(
            serializedBadge,
            Json.encodeToString(Trait.serializer(), badge)
        )
    }

    @Test
    fun deserializesBadge() {
        assertEquals(
            badge,
            Json.decodeFromString(Trait.serializer(), serializedBadge)
        )
    }

    @Test
    fun serializesAchievement() {
        assertEquals(
            serializedAchievement,
            Json.encodeToString(Trait.serializer(), achievement)
        )
    }

    @Test
    fun deserializesAchievement() {
        assertEquals(
            achievement,
            Json.decodeFromString(Trait.serializer(), serializedAchievement)
        )
    }
}