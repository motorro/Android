package com.motorro.background.pages.service

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.motorro.background.MyNotificationChannel
import com.motorro.background.R
import com.motorro.background.ServiceMonitor
import com.motorro.background.timer.data.TimerState
import com.motorro.background.timer.ui.formatTimer
import com.motorro.core.log.Logging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class TimerService : LifecycleService(), Logging {

    companion object {
        /**
         * Returns an [Intent] to start this service.
         */
        fun getStartIntent(context: Context): Intent = Intent(context, TimerService::class.java)

        /**
         * Service ID for monitoring
         */
        val serviceId = ServiceMonitor.ServiceId(requireNotNull(TimerService::class.qualifiedName))

        /**
         * Notification ID
         */
        private const val NOTIFICATION_ID = 100500
    }

    private var timerJob: Job? = null
    private val timerState = MutableStateFlow<TimerState>(TimerState.Stopped())

    // region -= Binding =-

    private val binder = ServiceBinder()
    override fun onBind(intent: Intent): IBinder? {
        status { "Call to bind service" }
        super.onBind(intent)
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        status { "Call to unbind service" }
        return true
    }

    override fun onRebind(intent: Intent?) {
        status { "Call to rebind service." }
    }

    inner class ServiceBinder : Binder() {
        fun getService() : TimerService {
            return this@TimerService
        }
    }

    // endregion

    // region -= Service =-

    val state: StateFlow<TimerState> get() = timerState.asStateFlow()

    fun toggle() {
        if (timerState.value is TimerState.Running) {
            stop()
        } else {
            start()
        }
    }

    fun start() {
        var currentTime = timerState.value.time
        timerJob?.cancel()
        timerJob = lifecycleScope.launch {
            flow {
                while(isActive) {
                    emit(TimerState.Running(currentTime))
                    currentTime += 1.seconds
                    delay(1.seconds)
                }
            }.collect(timerState)
        }
    }

    fun stop() {
        val currentTime = timerState.value.time
        timerJob?.cancel()
        timerState.value = TimerState.Stopped(currentTime)
    }

    // endregion

    // region -= Lifecycle =-

    override fun onCreate() {
        super.onCreate()
        status { "Service created." }
        startMonitor()
        startForeground()
        notifyStatus()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        status { "Service started." }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMonitor()
        stopForeground(STOP_FOREGROUND_REMOVE)
        status { "Service destroyed." }
    }

    private fun startForeground() {
        ServiceCompat.startForeground(
            /* service */ this,
            /* notification id */ NOTIFICATION_ID,
            /* notification*/ buildNotification(timerState.value),
            /* foreground type */ if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            } else {
                0
            }
        )
    }

    private fun buildNotification(state: TimerState): Notification {
        return NotificationCompat.Builder(this, MyNotificationChannel.ONGOING.name)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(
                when (state) {
                    is TimerState.Running -> getString(R.string.notification_timer_running, state.time.formatTimer())
                    is TimerState.Stopped -> getString(R.string.notification_timer_stopped, state.time.formatTimer())
                }
            )
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .build()
    }

    private fun notifyStatus() = lifecycleScope.launch {
        timerState.collect {
            notify(it)
        }
    }

    private fun notify(state: TimerState) {
        val notificationManager = NotificationManagerCompat.from(this.applicationContext)
        val notifications = notificationManager.activeNotifications
        val ongoingNotification = notifications.find { NOTIFICATION_ID == it.id }
        if (null != ongoingNotification && notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(NOTIFICATION_ID, buildNotification(state))
        }
    }

    // endregion

    // region -= Monitoring =-

    @Inject
    lateinit var serviceMonitor: ServiceMonitor

    fun startMonitor() = lifecycleScope.launch {
        broadcast(ServiceMonitor.Status.STARTED)
        while (isActive) {
            delay(500.milliseconds)
            broadcast(ServiceMonitor.Status.RUNNING)
        }
    }

    fun stopMonitor() = runBlocking {
        broadcast(ServiceMonitor.Status.STOPPED)
    }

    private suspend fun broadcast(message: ServiceMonitor.Status) {
        serviceMonitor.update(serviceId, message)
    }

    private inline fun status(crossinline message: () -> String) {
        i { message() + " State: ${timerState.value}" }
    }

    // endregion
}