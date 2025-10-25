package com.motorro.notifications.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.motorro.core.log.Logging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FcmMessagingService: FirebaseMessagingService(), Logging {

    @set:Inject
    lateinit var uploadToken: UploadTokenWorker.Scheduler

    override fun onNewToken(token: String) {
        i { "onNewToken: $token"  }
        uploadToken(FcmToken(token))
    }

    override fun onMessageReceived(message: RemoteMessage) {
        i { "onMessageReceived: $message"  }
    }
}