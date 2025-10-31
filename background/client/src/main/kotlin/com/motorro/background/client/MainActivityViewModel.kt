package com.motorro.background.client

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import com.motorro.background.timer.ITimerCallback
import com.motorro.background.timer.ITimerService
import com.motorro.background.timer.ITimerState
import com.motorro.background.timer.TimerServiceApi
import com.motorro.background.timer.data.TimerGesture
import com.motorro.background.timer.data.TimerState
import com.motorro.background.timer.data.toTimerState
import com.motorro.core.log.Logging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.properties.Delegates

class MainActivityViewModel(application: Application) : AndroidViewModel(application), Logging {

    private val timerState: MutableStateFlow<TimerState> = MutableStateFlow(TimerState.Stopped())
    private var serviceConnection: ServiceConnection? = null

    val uiState: StateFlow<TimerState> get() = timerState.asStateFlow()

    private var service: ITimerService? by Delegates.observable(null) { _, oldService, newService ->
        oldService?.unsubscribe(timerCallback)
        newService?.let {
            timerState.value = it.state.toTimerState()
            it.subscribe(timerCallback)
        }
    }

    private var timerCallback = object : ITimerCallback.Stub() {
        override fun onStateChange(state: ITimerState) {
            timerState.value = state.toTimerState()
        }
    }

    fun process(gesture: TimerGesture) {
        when(gesture) {
            TimerGesture.Toggle -> service?.toggle()
        }
    }

    init {
        bindService()
    }

    private fun bindService() {
        val connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, serviceBinder: IBinder) {
                service = ITimerService.Stub.asInterface(serviceBinder)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                i { "Service disconnected" }
                service = null
            }
        }

        i { "Binding service..." }
        getApplication<Application>().bindService(
            TimerServiceApi.getIntent(),
            connection,
            Context.BIND_AUTO_CREATE
        )

        serviceConnection = connection
    }

    override fun onCleared() {
        val connection = serviceConnection ?: return
        i { "Unbinding service..." }
        getApplication<Application>().unbindService(connection)
        serviceConnection = null
    }
}