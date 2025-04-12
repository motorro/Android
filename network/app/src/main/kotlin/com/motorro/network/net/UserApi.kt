package com.motorro.network.net

import com.motorro.network.data.Profile
import com.motorro.network.data.User
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API for user data
 */
interface UserApi {
    /**
     * Returns a list of users
     */
    @GET("users")
    suspend fun getUserList(): Result<List<User>>

    /**
     * Returns a list of users
     */
    @GET("profiles/{id}")
    suspend fun getProfile(@Path("id") userId: Int): Result<Profile>
}