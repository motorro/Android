package ru.merionet.tasks.app.net

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters
import kotlinx.coroutines.withContext
import ru.merionet.core.coroutines.DispatcherProvider
import ru.merionet.tasks.core.net.platformUrl
import ru.merionet.tasks.data.HttpResponse
import ru.merionet.tasks.data.TaskUpdateRequest
import ru.merionet.tasks.data.TaskUpdates
import ru.merionet.tasks.data.Version
import ru.merionet.tasks.data.VersionResponse
import ru.merionet.tasks.di.AppHttp
import javax.inject.Inject

/**
 * Tasks API
 */
interface TasksApi {
    /**
     * Get current task-list version
     */
    suspend fun getVersion(): HttpResponse<VersionResponse>

    /**
     * Get task updates
     */
    suspend fun getUpdates(currentVersion: Version? = null): HttpResponse<TaskUpdates>

    /**
     * Puts task updates to server
     */
    suspend fun postUpdates(request: TaskUpdateRequest): HttpResponse<VersionResponse>

    /**
     * Ktor API implementation
     */
    class Impl @Inject constructor(
        @AppHttp private val httpClient: HttpClient,
        private val dispatchers: DispatcherProvider
    ) : TasksApi {
        override suspend fun getVersion(): HttpResponse<VersionResponse> {
            val result = withContext(dispatchers.io) {
                httpClient.get {
                    platformUrl(listOf("tasks/version"))
                }
            }
            return result.body()
        }

        override suspend fun getUpdates(currentVersion: Version?): HttpResponse<TaskUpdates> {
            val result = withContext(dispatchers.io) {
                httpClient.get {
                    platformUrl(listOf("tasks/updates")) {
                        parameters {
                            currentVersion?.let {
                                parameters["version"] = it.value
                            }
                        }
                    }
                }
            }
            return result.body()
        }

        override suspend fun postUpdates(request: TaskUpdateRequest): HttpResponse<VersionResponse> {
            val result = withContext(dispatchers.io) {
                httpClient.post {
                    platformUrl(listOf("tasks/version"))
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            }
            return result.body()
        }
    }
}