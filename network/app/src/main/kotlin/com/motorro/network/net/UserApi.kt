package com.motorro.network.net

import com.motorro.network.data.Profile
import com.motorro.network.data.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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
     * Returns a user profile
     */
    @GET("profiles/{id}")
    suspend fun getProfile(@Path("id") userId: Int): Result<Profile>

    /**
     * Creates a user
     */
    @POST("users")
    suspend fun createUser(@Body profile: Profile, @Header("Authorization") token: String?): Result<User>

    /**
     * Returns a user profile
     */
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: Int, @Header("Authorization") token: String?)
}