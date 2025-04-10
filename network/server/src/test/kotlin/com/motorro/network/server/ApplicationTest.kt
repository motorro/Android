package com.motorro.network.server

import com.motorro.network.data.Phone
import com.motorro.network.data.Profile
import com.motorro.network.data.User
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.json.Json
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class ApplicationTest {

    private val json = Json

    private val profile = Profile(
        userId = 1,
        name = "Vasya",
        age = 25,
        registered = Instant.parse("2023-11-17T11:43:22.306Z"),
        phone = Phone(7, "1234567890"),
        userpic = URI("https://example.com/userpic.jpg")
    )

    private val user = User(
        userId = profile.userId,
        name = profile.name,
        userpic = profile.userpic
    )

    private val users: Users = mockk{
        every { getUsers() } returns listOf(user)
        every { getProfile(any()) } answers {
            val userId = firstArg<Int>()
            if (userId == profile.userId) {
                profile
            } else {
                throw NotFoundException("User with id $userId not found")
            }
        }
    }

    private fun ApplicationTestBuilder.prepareClient(): HttpClient {
        return createClient {
            install(ContentNegotiation) {
                json(Json)
            }
        }
    }

    @Test
    fun usersResponds() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().get("/users") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            listOf(user),
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun profileResponds() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().get("/profiles/${profile.userId}") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            profile,
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun failsOnProfileNotFoune() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().get("/profiles/100500") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}