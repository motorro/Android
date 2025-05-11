package com.motorro.cookbook.app

import android.app.Application
import com.motorro.cookbook.app.db.CookbookDao
import com.motorro.cookbook.app.db.CookbookDb
import com.motorro.cookbook.app.net.Config
import com.motorro.cookbook.app.net.ktorHttp
import com.motorro.cookbook.app.net.lenientJson
import com.motorro.cookbook.app.net.okhttp
import com.motorro.cookbook.app.net.retrofit
import com.motorro.cookbook.app.repository.CookbookApi
import com.motorro.cookbook.app.repository.KtorCookbookApi
import com.motorro.cookbook.app.repository.RecipeRepository
import com.motorro.cookbook.app.repository.RecipeRepositoryImpl
import com.motorro.cookbook.app.repository.usecase.AddRecipeUsecase
import com.motorro.cookbook.app.repository.usecase.AddRecipeUsecaseImpl
import com.motorro.cookbook.app.repository.usecase.CategoriesUsecase
import com.motorro.cookbook.app.repository.usecase.CategoriesUsecaseImpl
import com.motorro.cookbook.app.repository.usecase.DeleteRecipeUsecase
import com.motorro.cookbook.app.repository.usecase.DeleteRecipeUsecaseImpl
import com.motorro.cookbook.app.repository.usecase.RecipeListUsecase
import com.motorro.cookbook.app.repository.usecase.RecipeListUsecaseImpl
import com.motorro.cookbook.app.repository.usecase.RecipeUsecase
import com.motorro.cookbook.app.repository.usecase.RecipeUsecaseImpl
import com.motorro.cookbook.app.session.DatastoreSessionStorage
import com.motorro.cookbook.app.session.RetrofitUserApiImpl
import com.motorro.cookbook.app.session.RetrofitUserService
import com.motorro.cookbook.app.session.SessionManager
import com.motorro.cookbook.app.session.UserApi
import io.ktor.client.HttpClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import kotlin.time.Clock
import kotlin.uuid.Uuid

/**
 * The application. Provides access to dependencies.
 */
@OptIn(DelicateCoroutinesApi::class)
@Suppress("MemberVisibilityCanBePrivate", "unused")
class App : Application() {



    /**
     * Json instance
     */
    fun json(): Json = lenientJson()

    /**
     * OkHttp client
     */
    fun okHttpClient(): OkHttpClient = okhttp()

    /**
     * Authentication retrofit
     */
    val authRetrofit: Retrofit by lazy {
        retrofit(okHttpClient(), json(), Config.getBaseUrl())
    }

    /**
     * User API
     */
    fun userApi(): UserApi = RetrofitUserApiImpl(
        authRetrofit.create(RetrofitUserService::class.java)
    )

    /**
     * Session manager
     */
    val sessionManager: SessionManager by lazy {
        SessionManager.Impl(DatastoreSessionStorage(this), userApi(), GlobalScope)
    }

    /**
     * Ktor HTTP client
     */
    fun httpClient(): HttpClient = ktorHttp(okHttpClient(), json(), sessionManager)

    /**
     * Cookbook network API
     */
    fun cookbookApi(): CookbookApi = KtorCookbookApi(
        context = this,
        httpClient = httpClient(),
        baseUrl = Config.getBaseUrl().toUrl()
    )

    /**
     * Database instance
     */
    val db: CookbookDb by lazy { CookbookDb.create(this) }

    /**
     * Recipes DAO
     */
    fun recipesDao(): CookbookDao = db.recipesDao()

    /**
     * Recipe-list use-case
     */
    val recipeListUsecase: RecipeListUsecase by lazy {
        RecipeListUsecaseImpl(sessionManager, recipesDao(), cookbookApi(), GlobalScope)
    }

    fun recipeUsecaseFactory(): RecipeUsecase.Factory = object : RecipeUsecase.Factory {
        override fun invoke(recipeId: Uuid): RecipeUsecase = RecipeUsecaseImpl(
            recipeId,
            sessionManager,
            cookbookApi(),
            GlobalScope
        )
    }

    /**
     * Categories use-case
     */
    fun categoriesUsecase(): CategoriesUsecase = CategoriesUsecaseImpl(
        sessionManager,
        recipesDao()
    )

    /**
     * Add recipe use-case
     */
    fun addRecipeUsecase(): AddRecipeUsecase = AddRecipeUsecaseImpl(
        sessionManager,
        cookbookApi(),
        GlobalScope,
        Clock.System
    )

    /**
     * Delete recipe use-case
     */
    fun deleteRecipeUsecase(): DeleteRecipeUsecase = DeleteRecipeUsecaseImpl(
        sessionManager,
        cookbookApi(),
        GlobalScope
    )

    /**
     * The recipe repository.
     */
    val recipeRepository: RecipeRepository by lazy {
        RecipeRepositoryImpl(
            recipeListUsecase,
            recipeUsecaseFactory(),
            categoriesUsecase(),
            addRecipeUsecase(),
            deleteRecipeUsecase()
        )
    }
}