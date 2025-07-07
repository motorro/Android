package com.motorro.cookbook.data.db.di

import android.content.Context
import com.motorro.cookbook.data.db.CookbookDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {
    @Provides
    @Singleton
    fun cookbookDb(@Named("Application") context: Context): CookbookDb = CookbookDb.create(context)
}