package com.motorro.notifications.fcm.di

import com.motorro.notifications.fcm.PushTokenService
import com.motorro.notifications.fcm.UploadTokenWorker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FcmModule {
    @Binds
    abstract fun pushTokenService(impl: PushTokenService.Impl): PushTokenService

    @Binds
    abstract fun pushTokenWorkScheduler(impl: UploadTokenWorker.Scheduler.Impl): UploadTokenWorker.Scheduler
}