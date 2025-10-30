package com.motorro.background.pages.service.state

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.motorro.background.ServiceMonitor
import com.motorro.background.pages.service.TimerService
import com.motorro.background.pages.service.data.ServiceGesture
import com.motorro.background.pages.service.data.ServiceUiState
import com.motorro.background.timer.data.TimerGesture
import com.motorro.background.timer.data.TimerState
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.core.log.Logging
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

class ServiceState(
    private val context: ServiceContext,
    private val serviceMonitor: ServiceMonitor,
): CoroutineState<ServiceGesture, ServiceUiState>(), ServiceContext by context, Logging {

    private val timerState: MutableStateFlow<TimerState> = MutableStateFlow(TimerState.Stopped())
    private var timerJob: Job? = null

    private var isServiceRunning: Boolean? by Delegates.observable(null) {_, _, newValue ->
        if (false == newValue) {
            service = null
        }
        render()
    }

    private var service: TimerService? by Delegates.observable(null) { _, _, newValue ->
        if (null != newValue) {
            listenTime(newValue)
        } else {
            stopListenTime()
        }
        render()
    }

    private fun isBound() = null != service

    private fun listenTime(service: TimerService) {
        timerJob?.cancel()
        timerJob = stateScope.launch {
            service.state.collect(timerState)
        }
    }

    private fun stopListenTime() {
        timerJob?.cancel()
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, serviceBinder: IBinder) {
            i { "Service connected" }
            service = (serviceBinder as TimerService.ServiceBinder).getService()
        }

        // Not being called when unbinding service
        // See parent method
        override fun onServiceDisconnected(name: ComponentName?) {
            i { "Service disconnected" }
            service = null
        }
    }

    override fun doStart() {
        startMonitoring()
        timerState.onEach { render() }.launchIn(stateScope)
    }

    override fun doClear() {
        super.doClear()
        unbindService()
    }

    private fun bindService() {
        if (isBound().not()) {
            i { "Binding service..." }
            appContext.bindService(
                TimerService.getStartIntent(appContext),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun unbindService() {
        if (isBound()) {
            i { "Unbinding service..." }
            appContext.unbindService(serviceConnection)
            service = null
        }
    }

    private fun startService() {
        if (true == isServiceRunning) {
            i { "Service already running" }
            return
        }
        i { "Starting timer service..." }
        try {
            appContext.startForegroundService(TimerService.getStartIntent(appContext))
        } catch (e: Throwable) {
            w(e) { "Failed to start service" }
        }
    }

    private fun stopService() {
        val result = appContext.stopService(TimerService.getStartIntent(appContext))
        i { "Service found and stopped: $result" }
        if (result) {
            service = null
        }
    }

    override fun doProcess(gesture: ServiceGesture) {
        when(gesture) {
            ServiceGesture.StartService -> startService()
            ServiceGesture.StopService -> stopService()
            ServiceGesture.BindService -> bindService()
            ServiceGesture.UnbindService -> unbindService()
            is ServiceGesture.Timer -> when(gesture.child) {
                TimerGesture.Toggle -> service?.toggle()
            }
        }
    }

    private fun render() {
        setUiState(
            ServiceUiState(
                timerState = timerState.value,
                hasServiceStatus = null != isServiceRunning,
                isServiceRunning = true == isServiceRunning,
                isServiceBound = isBound()
            )
        )
    }

    // region -= Monitoring =-

    private fun startMonitoring() = stateScope.launch {
        serviceMonitor.monitorRunning(TimerService.serviceId).collect {
            i { "Status update: Service running: $it" }
            isServiceRunning = it
        }
    }

    // endregion

    class Factory @Inject constructor(private val serviceMonitor: ServiceMonitor) {
        operator fun invoke(context: ServiceContext) = ServiceState(context, serviceMonitor)
    }
}