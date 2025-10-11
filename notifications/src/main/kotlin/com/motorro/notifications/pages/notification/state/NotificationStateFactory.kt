package com.motorro.notifications.pages.notification.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.notifications.pages.notification.data.NotificationData
import javax.inject.Inject

interface NotificationStateFactory {
    fun form(): NotificationState
    fun creating(data: NotificationData): NotificationState

    class Impl @Inject constructor(savedStateHandle: SavedStateHandle) : NotificationStateFactory {

        private val context = object : NotificationContext {
            override val factory: NotificationStateFactory = this@Impl
            override val savedStateHandle: SavedStateHandle = savedStateHandle
        }

        override fun form() = FormState(context)

        override fun creating(data: NotificationData): NotificationState {
            TODO("Not yet implemented")
        }
    }
}

