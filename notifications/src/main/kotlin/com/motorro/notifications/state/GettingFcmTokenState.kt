package com.motorro.notifications.state

import android.content.Intent
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.motorro.core.log.Logging
import com.motorro.notifications.data.MainScreenGesture
import com.motorro.notifications.data.MainScreenViewState
import com.motorro.notifications.fcm.FcmToken
import com.motorro.notifications.fcm.UploadTokenWorker
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class GettingFcmTokenState @Inject constructor(
    context: MainScreenContext,
    private val intent: Intent,
    private val uploadToken: UploadTokenWorker.Scheduler
) : BaseMainScreenState(context), Logging {

    override fun doStart() {
        super.doStart()
        setUiState(MainScreenViewState.Loading)
        register()
    }

    private fun register() = stateScope.launch {
        try {
            d { "Getting token from FCM..." }
            val token = getToken()
            d { "Registering token: $token" }
            uploadToken(getToken())
            d { "Token registered" }
            setMachineState(factory.startUp(intent))
        } catch (e: Throwable) {
            w(e) { "Failed to register FCM" }
            setMachineState(factory.fcmRegistrationError(e, intent))
        }
    }

    private suspend fun getToken(): FcmToken = suspendCoroutine { continuation ->
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resumeWith(Result.success(FcmToken(task.result)))
                return@addOnCompleteListener
            }
            continuation.resumeWith(Result.failure(task.exception ?: Exception("Unknown error registering FCM")))
        }
    }

    override fun doProcess(gesture: MainScreenGesture) {
        when(gesture) {
            is MainScreenGesture.Back -> {
                d { "Back pressed. Terminating..." }
                setMachineState(factory.terminated())
            }
            else -> super.doProcess(gesture)
        }
    }

    class Factory @Inject constructor(private val uploadToken: UploadTokenWorker.Scheduler) {
        operator fun invoke(context: MainScreenContext, intent: Intent) = GettingFcmTokenState(
            context,
            intent,
            uploadToken
        )
    }
}