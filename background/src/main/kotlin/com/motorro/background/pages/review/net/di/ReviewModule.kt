package com.motorro.background.pages.review.net.di

import com.motorro.background.pages.review.net.ReviewApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ReviewModule {
    @Binds
    abstract fun reviewApi(impl: ReviewApi.Impl): ReviewApi
}