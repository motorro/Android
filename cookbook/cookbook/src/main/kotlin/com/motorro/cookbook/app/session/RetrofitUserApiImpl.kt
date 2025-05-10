package com.motorro.cookbook.app.session

import com.motorro.cookbook.app.data.CookbookError
import com.motorro.cookbook.data.Profile
import okhttp3.Credentials
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Header

class RetrofitUserApiImpl(private val userService: RetrofitUserService) : UserApi {
    override suspend fun getProfile(username: String, password: String): Profile {
        // Builds 'Authorization' header value
        val credentials: String = Credentials.basic(username, password)
        // Gets profile using credentials
        return try { userService.getProfile(credentials) } catch (e: Throwable) {
            throw when {
                e is HttpException && 401 == e.code() -> CookbookError.Unauthorized(e.message ?: "Not authorized")
                else -> CookbookError.Unknown(e)
            }
        }
    }
}

/**
 * Retrofit service
 */
interface RetrofitUserService {
    /**
     * Gets user profile
     * This method demonstrates how to create custom headers
     */
    @GET("profile")
    suspend fun getProfile(@Header("Authorization") credentials: String): Profile
}