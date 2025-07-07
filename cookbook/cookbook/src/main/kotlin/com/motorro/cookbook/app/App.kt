package com.motorro.cookbook.app

import android.app.Application
import com.motorro.cookbook.data.db.CookbookDb
import com.motorro.cookbook.data.net.Config
import com.motorro.cookbook.data.recipes.CookbookApi
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
import com.motorro.cookbook.domain.session.SessionManager
import dagger.hilt.android.HiltAndroidApp
import io.ktor.client.HttpClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.uuid.Uuid

/**
 * The application. Provides access to dependencies.
 */
@OptIn(DelicateCoroutinesApi::class)
@Suppress("MemberVisibilityCanBePrivate", "unused")
@HiltAndroidApp
class App : Application() {

    @set:Inject
    lateinit var sessionManager: SessionManager

    @set:Inject
    lateinit var httpClient: HttpClient

    /**
     * Cookbook network API
     */
    fun cookbookApi(): CookbookApi = KtorCookbookApi(
        context = this,
        httpClient = httpClient,
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
}