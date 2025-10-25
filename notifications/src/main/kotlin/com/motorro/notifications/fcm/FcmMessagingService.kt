package com.motorro.notifications.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.motorro.core.log.Logging
import com.motorro.notifications.NotificationActionBuilder
import com.motorro.notifications.NotificationActionBus
import com.motorro.notifications.data.NotificationAction
import com.motorro.notifications.pages.push.api.PushPageData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FcmMessagingService: FirebaseMessagingService(), Logging {

    @set:Inject
    lateinit var uploadToken: UploadTokenWorker.Scheduler

    @set:Inject
    lateinit var notificationActionBus: NotificationActionBus

    @set:Inject
    lateinit var notificationActionBuilder: NotificationActionBuilder

    override fun onNewToken(token: String) {
        i { "onNewToken: $token"  }
        uploadToken(FcmToken(token))
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val notification = message.notification
        val data = message.data[PushPageData.PUSH_DATA_PARAM]

        i { "Message received" }
        i { "Notification: $notification" }
        i { "Data: ${message.data}" }

        notificationActionBus.post(
            NotificationAction(
                notificationActionBuilder.push(
                    title = notification?.title.orEmpty(),
                    message = notification?.body.orEmpty(),
                    data = data
                )
            )
        )
    }
}