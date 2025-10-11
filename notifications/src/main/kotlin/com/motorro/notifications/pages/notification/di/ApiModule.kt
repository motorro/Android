package com.motorro.notifications.pages.notification.di

import com.motorro.notifications.api.MainScreenStateApi
import com.motorro.notifications.api.MainScreenUiApi
import com.motorro.notifications.pages.notification.api.NotificationStateApi
import com.motorro.notifications.pages.notification.api.NotificationUiApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityComponent::class)
abstract class ApiUiModule {
    @Binds
    @IntoSet
    abstract fun notificationScreen(impl: NotificationUiApi): MainScreenUiApi
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ApiStateModule {
    @Binds
    @IntoSet
    abstract fun notificationState(impl: NotificationStateApi): MainScreenStateApi<*, *>
}