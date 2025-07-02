package com.motorro.cookbook.app

import android.app.Application
import com.motorro.cookbook.app.navigation.AppLinks
import com.motorro.cookbook.appcore.di.DiContainer
import com.motorro.cookbook.appcore.navigation.Links
import com.motorro.cookbook.data.db.CookbookDb
import com.motorro.cookbook.data.net.Config
import com.motorro.cookbook.data.net.ktorHttp
import com.motorro.cookbook.data.net.lenientJson
import com.motorro.cookbook.data.net.okhttp
import com.motorro.cookbook.data.net.retrofit
import com.motorro.cookbook.data.recipes.CookbookApi
import com.motorro.cookbook.data.recipes.RecipeRepositoryImpl
import com.motorro.cookbook.data.recipes.db.CookbookDao
import com.motorro.cookbook.data.recipes.net.KtorCookbookApi
import com.motorro.cookbook.data.recipes.usecase.AddRecipeUsecase
import com.motorro.cookbook.data.recipes.usecase.AddRecipeUsecaseImpl
import com.motorro.cookbook.data.recipes.usecase.CategoriesUsecase
import com.motorro.cookbook.data.recipes.usecase.CategoriesUsecaseImpl
import com.motorro.cookbook.data.recipes.usecase.DeleteRecipeUsecase
import com.motorro.cookbook.data.recipes.usecase.DeleteRecipeUsecaseImpl
import com.motorro.cookbook.data.recipes.usecase.RecipeListUsecase
import com.motorro.cookbook.data.recipes.usecase.RecipeListUsecaseImpl
import com.motorro.cookbook.data.recipes.usecase.RecipeUsecase
import com.motorro.cookbook.data.recipes.usecase.RecipeUsecaseImpl
import com.motorro.cookbook.data.session.DatastoreSessionStorage
import com.motorro.cookbook.data.session.RetrofitUserApiImpl
import com.motorro.cookbook.data.session.RetrofitUserService
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.domain.session.SessionManagerImpl
import com.motorro.cookbook.domain.session.UserApi
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
class App : Application(), DiContainer {

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
    override val sessionManager: SessionManager by lazy {
        SessionManagerImpl(DatastoreSessionStorage(this), userApi(), GlobalScope)
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
        override fun invoke(recipeId: Uuid): RecipeUsecase =
            RecipeUsecaseImpl(
                recipeId,
                sessionManager,
                recipesDao(),
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
        recipesDao(),
        cookbookApi(),
        GlobalScope,
        Clock.System
    )

    /**
     * Delete recipe use-case
     */
    fun deleteRecipeUsecase(): DeleteRecipeUsecase =
        DeleteRecipeUsecaseImpl(
            sessionManager,
            recipesDao(),
            cookbookApi(),
            GlobalScope
        )

    /**
     * The recipe repository.
     */
    override val recipeRepository: RecipeRepository by lazy {
        RecipeRepositoryImpl(
            recipeListUsecase,
            recipeUsecaseFactory(),
            categoriesUsecase(),
            addRecipeUsecase(),
            deleteRecipeUsecase()
        )
    }

    /**
     * Application deep-links
     */
    override val links: Links by lazy {
        AppLinks(this)
    }
}