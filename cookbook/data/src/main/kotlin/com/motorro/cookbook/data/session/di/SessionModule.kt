package com.motorro.cookbook.data.session.di

import android.content.Context
import com.motorro.cookbook.data.session.DatastoreSessionStorage
import com.motorro.cookbook.data.session.RetrofitUserApiImpl
import com.motorro.cookbook.data.session.RetrofitUserService
import com.motorro.cookbook.domain.session.SessionStorage
import com.motorro.cookbook.domain.session.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class SessionModule {
    @Provides
    @Singleton
    fun sessionStorage(context: Context): SessionStorage = DatastoreSessionStorage(context)

    @Provides
    fun userService(retrofit: Retrofit): RetrofitUserService = retrofit.create(RetrofitUserService::class.java)

    @Provides
    fun userApi(service: RetrofitUserService): UserApi = RetrofitUserApiImpl(service)
}