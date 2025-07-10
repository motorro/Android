package com.motorro.cookbook.recipedemo.di

import android.app.Application
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import com.motorro.cookbook.appcore.R
import com.motorro.cookbook.appcore.navigation.Links
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import javax.inject.Named
import kotlin.uuid.Uuid

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Named("Application")
    fun applicationScope(): CoroutineScope = MainScope()

    @Provides
    fun links(application: Application): Links = object : Links {
        override fun login(): NavDeepLinkRequest = TODO("Login not implemented")

        override fun recipe(id: Uuid): NavDeepLinkRequest = NavDeepLinkRequest.Builder
            .fromUri(application.getString(R.string.link_recipe).replace("{recipeId}", id.toString()).toUri())
            .build()

        override fun addRecipe(): NavDeepLinkRequest = TODO("Add recipe not implemented")
    }
}