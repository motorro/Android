package com.motorro.network.net

import com.motorro.network.data.Profile
import com.motorro.network.data.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import retrofit2.http.Body
import retrofit2.http.Path

/**
 * Retrofit API for user data
 */
class KtorUserApi(private val httpClient: HttpClient) : UserApi {
    /**
     * Returns a list of users
     */
    override suspend fun getUserList(): Result<List<User>> = coRunCatching {
        httpClient.get(Config.getBaseUrl().toUrl()) {
            url {
                appendPathSegments("users")
            }
        }.body()
    }

    /**
     * Returns a user profile
     */
    override suspend fun getProfile(userId: Int): Result<Profile> = coRunCatching {
        httpClient.get(Config.getBaseUrl().toUrl()) {
            url {
                appendPathSegments("profiles", userId.toString())
            }
        }.body()
    }

    /**
     * Creates a user
     */
    override suspend fun createUser(@Body profile: Profile): Result<User> = coRunCatching {
        httpClient.post(Config.getBaseUrl().toUrl()) {
            url {
                appendPathSegments("users")
                contentType(ContentType.Application.Json)
            }
            setBody(profile)
        }.body()
    }

    /**
     * Returns a user profile
     */
    override suspend fun deleteUser(@Path("id") userId: Int) {
        coRunCatching {
            httpClient.delete(Config.getBaseUrl().toUrl()) {
                url {
                    appendPathSegments("users", userId.toString())
                }
            }
        }
    }
}