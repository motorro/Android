package ru.merionet.tasks.server

import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import ru.merionet.tasks.data.AuthRequest
import ru.merionet.tasks.data.ErrorCode
import ru.merionet.tasks.data.HttpResponse
import ru.merionet.tasks.data.SessionClaims
import ru.merionet.tasks.data.httpResponseModule
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    private val json = Json {
        serializersModule = httpResponseModule
    }

    @Test
    fun rootResponds() = testApplication {
        application {
            module()
        }
        val response = client.get("/") {
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
        val response = client.post("/login") {
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
        val response = client.post("/login") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            setBody(
                json.encodeToString(AuthRequest.serializer(), AuthRequest("cool", "hacker"))
            )
        }
        assertEquals(HttpStatusCode.Forbidden, response.status)
        assertEquals(
            HttpResponse.Error(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.defaultMessage),
            json.decodeFromString<HttpResponse<SessionClaims>>(response.bodyAsText())
        )
    }
}