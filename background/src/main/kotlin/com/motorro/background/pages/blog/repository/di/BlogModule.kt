package com.motorro.background.pages.blog.repository.di

import com.motorro.background.pages.blog.repository.BlogApi
import com.motorro.background.pages.blog.repository.BlogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BlogModule {
    @Binds
    abstract fun blogApi(impl: BlogApi.Impl): BlogApi

    @Binds
    @Singleton
    abstract fun blogRepository(impl: BlogRepository.Impl): BlogRepository
}