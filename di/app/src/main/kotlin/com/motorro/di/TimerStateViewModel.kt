package com.motorro.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.motorro.di.timer.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class TimerStateViewModel @Inject constructor(
    @Named("app") private val timer: Timer,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    /**
     * Current time
     */
    val currentTime: StateFlow<Duration> get() = timer.count

    /**
     * Provided time
     */
    val providedTime: StateFlow<Duration> = MutableStateFlow(
        (savedStateHandle.get<Long>(KEY_PROVIDED_TIME) ?: 0).milliseconds
    )

    companion object {
        const val KEY_PROVIDED_TIME = "providedTime"
    }
}