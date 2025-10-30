package com.motorro.background.pages.service

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.motorro.background.ServiceMonitor
import com.motorro.background.timer.data.TimerState
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
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        status { "Service started." }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMonitor()
        status { "Service destroyed." }
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