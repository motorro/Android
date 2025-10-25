package com.motorro.notifications.fcm

import com.motorro.core.log.Logging
import javax.inject.Inject

/**
 * Server API to register push token at application server
 */
interface PushTokenService {
    suspend fun register(token: FcmToken)

    class Impl @Inject constructor(): PushTokenService, Logging {
        override suspend fun register(token: FcmToken) {
            i { "********* Emulating push token registration ************" }
            i { "Registering token: $token" }
            i { "********************************************************" }
        }
    }
}
