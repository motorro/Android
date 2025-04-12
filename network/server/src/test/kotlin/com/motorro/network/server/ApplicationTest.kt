package com.motorro.network.server

import com.motorro.network.data.Phone
import com.motorro.network.data.Profile
import com.motorro.network.data.User
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

    private val newProfileId = 100500

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
        every { addUser(any()) } answers {
            val newProfile = firstArg<Profile>()
            user.copy(
                userId = newProfileId,
                name = newProfile.name,
                userpic = newProfile.userpic
            )
        }
        every { deleteUser(any()) } answers {
            val userId = firstArg<Int>()
            if (userId == profile.userId) {
                Unit
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
    fun failsOnProfileNotFound() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().get("/profiles/100500") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun addsUser() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().post("/users") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(profile)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        verify { users.addUser(profile) }
        assertEquals(
            user.copy(userId = newProfileId),
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun deletesUser() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().delete("/users/${profile.userId}") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
        verify { users.deleteUser(profile.userId) }
    }

    @Test
    fun failsToDeleteOnUserNotFound() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().delete("/users/100500") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}