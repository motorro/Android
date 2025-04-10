package com.motorro.network.server

import com.motorro.network.data.Phone
import com.motorro.network.data.Profile
import com.motorro.network.data.Trait
import java.net.URI
import kotlin.time.Instant


const val SERVER_HOST = "0.0.0.0"
const val SERVER_PORT = 8080

val profiles = listOf(
    Profile(
        userId = 1,
        name = "Vasya",
        age = 25,
        phone = Phone(7, "1234567890"),
        registered = Instant.parse("2023-11-17T11:43:22.306Z"),
        userpic = URI("https://picsum.photos/id/22/200/200"),
        interests = setOf("fishing", "coroutines", "soccer"),
        traits = listOf(
            Trait.PowerUser,
            Trait.Badge("Fisherman"),
            Trait.Achievement("GoalKeeper", 10)
        )
    ),
    Profile(
        userId = 2,
        name = "Masha",
        age = 30,
        phone = Phone(7, "9876543210"),
        registered = Instant.parse("2023-11-17T11:43:22.306Z"),
        userpic = URI("https://picsum.photos/id/25/200/200"),
        interests = setOf("hiking", "cooking", "chess"),
        traits = listOf(
            Trait.Badge("Chef"),
            Trait.Achievement("Hiker", 5)
        )
    )
)