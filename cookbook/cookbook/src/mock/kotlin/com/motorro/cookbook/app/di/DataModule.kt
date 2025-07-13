package com.motorro.cookbook.app.di

import com.motorro.cookbook.data.recipes.MockRepository
import com.motorro.cookbook.data.recipes.RECIPES
import com.motorro.cookbook.data.session.MemorySessionStorage
import com.motorro.cookbook.data.session.MockUserApi
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.domain.session.SessionStorage
import com.motorro.cookbook.domain.session.UserApi
import com.motorro.cookbook.domain.session.data.Session
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun sessionStorage(): SessionStorage = MemorySessionStorage(Session.None)

    @Provides
    fun userApi(): UserApi = MockUserApi()

    @Provides
    @Singleton
    fun recipeRepository(sessionManager: SessionManager): RecipeRepository = MockRepository(
        sessionManager,
        recipes = RECIPES,
        hasError = false
    )
}