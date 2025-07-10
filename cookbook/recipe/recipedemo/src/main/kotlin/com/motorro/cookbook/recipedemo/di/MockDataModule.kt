package com.motorro.cookbook.recipedemo.di

import com.motorro.cookbook.data.recipes.MockRepository
import com.motorro.cookbook.data.session.MemorySessionStorage
import com.motorro.cookbook.data.session.MockUserApi
import com.motorro.cookbook.data.session.SESSION
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.domain.session.SessionStorage
import com.motorro.cookbook.domain.session.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MockDataModule {
    @Provides
    @Singleton
    fun sessionStorage(): SessionStorage = MemorySessionStorage(SESSION)

    @Provides
    fun userApi(): UserApi = MockUserApi()

    @Provides
    fun recipeRepository(sessionManager: SessionManager): RecipeRepository = MockRepository(
        sessionManager
    )
}