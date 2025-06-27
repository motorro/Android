package com.motorro.di

import androidx.lifecycle.ViewModel
import com.motorro.di.timer.Timer
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Named
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel(assistedFactory = TimerStateViewModel.Factory::class)
class TimerStateViewModel @AssistedInject constructor(
    @Named("app") private val timer: Timer,
    @Assisted providedState: Long
) : ViewModel() {
    /**
     * Current time
     */
    val currentTime: StateFlow<Duration> get() = timer.count

    /**
     * Provided time
     */
    val providedTime: StateFlow<Duration> = MutableStateFlow(providedState.milliseconds)

    @AssistedFactory
    interface Factory {
        fun create(providedDuration: Long): TimerStateViewModel
    }
}