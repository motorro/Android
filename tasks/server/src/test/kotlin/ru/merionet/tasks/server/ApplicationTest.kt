package ru.merionet.tasks.server

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import ru.merionet.tasks.data.AuthRequest
import ru.merionet.tasks.data.ErrorCode
import ru.merionet.tasks.data.HttpResponse
import ru.merionet.tasks.data.SessionClaims
import ru.merionet.tasks.data.TaskCommand
import ru.merionet.tasks.data.TaskUpdateRequest
import ru.merionet.tasks.data.TaskUpdates
import ru.merionet.tasks.data.UserName
import ru.merionet.tasks.data.Version
import ru.merionet.tasks.data.VersionResponse
import ru.merionet.tasks.data.httpResponseModule
import ru.merionet.tasks.data.nextVersion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ApplicationTest {

    private val json = Json {
        serializersModule = httpResponseModule
    }
    

    private fun ApplicationTestBuilder.prepareClient(): HttpClient {
        return createClient {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    private suspend fun ApplicationTestBuilder.getUpdates(): TaskUpdates {
        val response = prepareClient().get("/tasks/updates") {
            accept(ContentType.Application.Json)
            bearerAuth(TOKEN)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val data = json.decodeFromString<HttpResponse<TaskUpdates>>(response.bodyAsText())
        assertIs<HttpResponse.Data<TaskUpdates>>(data)
        return data.data
    }

    private suspend fun ApplicationTestBuilder.getCurrentVersion(): VersionResponse {
        val response = prepareClient().get("/tasks/version") {
            accept(ContentType.Application.Json)
            bearerAuth(TOKEN)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val data = json.decodeFromString<HttpResponse<VersionResponse>>(response.bodyAsText())
        assertIs<HttpResponse.Data<VersionResponse>>(data)
        return data.data
    }

    @Test
    fun rootResponds() = testApplication {
        application {
            module()
        }
        val response = prepareClient().get("/") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(HttpResponse.Data("Hello World"), json.decodeFromString(response.bodyAsText()) )
    }

    @Test
    fun logsInIfCorrect() = testApplication {
        application {
            module()
        }
        val response = prepareClient().post("/login") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            setBody(
                json.encodeToString(AuthRequest.serializer(), AuthRequest(USERNAME, PASSWORD))
            )
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            HttpResponse.Data(SessionClaims(USERNAME, TOKEN)),
            json.decodeFromString<HttpResponse<SessionClaims>>(response.bodyAsText())
        )
    }

    @Test
    fun failsLoginIfIncorrectCredentials() = testApplication {
        application {
            module()
        }
        val response = prepareClient().post("/login") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            setBody(
                json.encodeToString(AuthRequest.serializer(), AuthRequest(UserName("cool"), "hacker"))
            )
        }
        assertEquals(HttpStatusCode.Forbidden, response.status)
        assertEquals(
            HttpResponse.Error(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.defaultMessage),
            json.decodeFromString<HttpResponse<SessionClaims>>(response.bodyAsText())
        )
    }

    @Test
    fun returnsTaskVersionForAuthorizedUsers() = testApplication {
        application {
            module()
        }
        val response = prepareClient().get("/tasks/version") {
            accept(ContentType.Application.Json)
            bearerAuth(TOKEN)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val tasks = json.decodeFromString<HttpResponse<VersionResponse>>(response.bodyAsText())
        assertIs<HttpResponse.Data<VersionResponse>>(tasks)
    }

    @Test
    fun failsToGetTaskVersionForNonAuthorizedUser() = testApplication {
        application {
            module()
        }
        val response = prepareClient().get("/tasks/version") {
            accept(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(
            HttpResponse.Error(ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.defaultMessage),
            json.decodeFromString<HttpResponse<VersionResponse>>(response.bodyAsText())
        )
    }

    @Test
    fun returnsTaskChangesForAuthorizedUsers() = testApplication {
        application {
            module()
        }
        val response = prepareClient().get("/tasks/updates") {
            accept(ContentType.Application.Json)
            bearerAuth(TOKEN)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val tasks = json.decodeFromString<HttpResponse<TaskUpdates>>(response.bodyAsText())
        assertIs<HttpResponse.Data<TaskUpdates>>(tasks)
    }

    @Test
    fun failsToGetTaskChangesForNonAuthorizedUser() = testApplication {
        application {
            module()
        }
        val response = prepareClient().get("/tasks/updates") {
            accept(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(
            HttpResponse.Error(ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.defaultMessage),
            json.decodeFromString<HttpResponse<TaskUpdates>>(response.bodyAsText())
        )
    }

    @Test
    fun registersChangesForAuthorizedUser() = testApplication {
        application {
            module()
        }
        val versionBefore = getCurrentVersion()
        val newTask = createTask()
        val command = TaskCommand.Upsert(newTask)
        val response = prepareClient().post("/tasks/updates") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            bearerAuth(TOKEN)
            setBody(TaskUpdateRequest(
                versionBefore.latestVersion,
                nextVersion(),
                listOf(command)
            ))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val versionAfter = json.decodeFromString<HttpResponse<VersionResponse>>(response.bodyAsText())
        assertIs<HttpResponse.Data<VersionResponse>>(versionAfter)

        assertNotEquals(versionBefore, versionAfter.data)

        val updates = getUpdates()
        assertTrue {
            updates.commands.contains(command)
        }
    }

    @Test
    fun failsToAddTaskForNonAuthorizedUser() = testApplication {
        application {
            module()
        }
        val versionBefore = getCurrentVersion()
        val newTask = createTask()
        val command = TaskCommand.Upsert(newTask)
        val response = prepareClient().post("/tasks/updates") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(TaskUpdateRequest(
                versionBefore.latestVersion,
                nextVersion(),
                listOf(command)
            ))
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(
            HttpResponse.Error(ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.defaultMessage),
            json.decodeFromString<HttpResponse<VersionResponse>>(response.bodyAsText())
        )
    }

    @Test
    fun failsToAddTaskForAuthorizedUserAndWrongVersion() = testApplication {
        application {
            module()
        }
        val newTask = createTask()
        val command = TaskCommand.Upsert(newTask)
        val response = prepareClient().post("/tasks/updates") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            bearerAuth(TOKEN)
            setBody(TaskUpdateRequest(
                Version("unknown"),
                nextVersion(),
                listOf(command)
            ))
        }
        assertEquals(HttpStatusCode.Conflict, response.status)
    }
}