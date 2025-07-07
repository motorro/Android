package com.motorro.cookbook.data.session

import com.motorro.cookbook.core.error.UnknownException
import com.motorro.cookbook.domain.session.UserApi
import com.motorro.cookbook.domain.session.error.UnauthorizedException
import com.motorro.cookbook.model.Profile
import okhttp3.Credentials
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Header

internal class RetrofitUserApiImpl(private val userService: RetrofitUserService) : UserApi {
    override suspend fun getProfile(username: String, password: String): Profile {
        // Builds 'Authorization' header value
        val credentials: String = Credentials.basic(username, password)
        // Gets profile using credentials
        return try { userService.getProfile(credentials) } catch (e: Throwable) {
            throw when {
                e is HttpException && 401 == e.code() -> UnauthorizedException(e)
                else -> UnknownException(e)
            }
        }
    }
}

/**
 * Retrofit service
 */
internal interface RetrofitUserService {
    /**
     * Gets user profile
     * This method demonstrates how to create custom headers
     */
    @GET("profile")
    suspend fun getProfile(@Header("Authorization") credentials: String): Profile
}