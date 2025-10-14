package com.motorro.notifications.pages.notification.state

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.motorro.notifications.pages.notification.data.NotificationData
import javax.inject.Inject

interface NotificationStateFactory {
    fun form(): NotificationState
    fun sending(toSend: NotificationData): NotificationState
    fun dismissingLatest(): NotificationState

    class Impl @Inject constructor(
        savedStateHandle: SavedStateHandle,
        app: Application
    ) : NotificationStateFactory {

        private val context = object : NotificationContext {
            override val factory: NotificationStateFactory = this@Impl
            override val savedStateHandle: SavedStateHandle = savedStateHandle
            override val androidContext: Context = app.applicationContext
        }

        override fun form(): NotificationState = FormState(context)
        override fun sending(toSend: NotificationData) = SendingNotification(context, toSend)
        override fun dismissingLatest() = DismissingLatestNotification(context)
    }
}

