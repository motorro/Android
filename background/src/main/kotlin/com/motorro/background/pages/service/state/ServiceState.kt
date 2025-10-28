package com.motorro.background.pages.service.state

import com.motorro.background.pages.service.data.ServiceGesture
import com.motorro.background.pages.service.data.ServiceUiState
import com.motorro.background.timer.data.TimerGesture
import com.motorro.background.timer.data.TimerState
import com.motorro.commonstatemachine.coroutines.CoroutineState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import kotlin.time.Duration.Companion.seconds

class ServiceState(private val context: ServiceContext): CoroutineState<ServiceGesture, ServiceUiState>() {

    private var timerJob: Job? = null

    override fun doStart() {
        render()
    }

    private var timerState: TimerState by Delegates.observable(TimerState.Stopped()) { _, _, _ ->
        render()
    }

    override fun doProcess(gesture: ServiceGesture) {
        when(gesture) {
            is ServiceGesture.Timer -> when(gesture.child) {
                TimerGesture.Toggle -> startStop()
            }
        }
    }

    private fun startStop() {
        when(timerState) {
            is TimerState.Running -> {
                timerJob?.cancel()
                timerState = TimerState.Stopped(timerState.time)
            }
            is TimerState.Stopped -> {
                timerJob = stateScope.launch {
                    var time = timerState.time
                    while (isActive) {
                        time += 1.seconds
                        timerState = TimerState.Running(time)
                        delay(1.seconds)
                    }
                }
            }
        }
    }

    private fun render() {
        setUiState(ServiceUiState(timerState))
    }
}