package com.motorro.cookbook.data.recipes.di

import com.motorro.cookbook.data.db.CookbookDb
import com.motorro.cookbook.data.net.Config
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
import com.motorro.cookbook.data.recipes.usecase.work.RecipeListSyncScheduler
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.session.UserHandler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import java.net.URL
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
internal class RecipesModule {
    @Provides
    fun recipesDao(db: CookbookDb): CookbookDao = db.recipesDao()

    @Provides
    @Named("Cookbook")
    fun url(): URL = Config.getBaseUrl().toUrl()
}

@Module
@InstallIn(SingletonComponent::class)
internal interface RecipesBindingModule {
    @Binds
    fun cookbookApi(impl: KtorCookbookApi): CookbookApi

    @Binds
    fun recipeListUsecase(impl: RecipeListUsecaseImpl): RecipeListUsecase

    @Binds
    fun categoriesUsecase(impl: CategoriesUsecaseImpl): CategoriesUsecase

    @Binds
    fun addRecipeUsecase(impl: AddRecipeUsecaseImpl): AddRecipeUsecase

    @Binds
    fun deleteRecipeUsecase(impl: DeleteRecipeUsecaseImpl): DeleteRecipeUsecase

    @Binds
    fun recipeRepository(impl: RecipeRepositoryImpl): RecipeRepository

    @Binds
    @IntoSet
    fun recipeListSyncScheduler(impl: RecipeListSyncScheduler): UserHandler
}

