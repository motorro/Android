package com.motorro.di

import androidx.lifecycle.ViewModel
import com.motorro.di.timer.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

/**
 * Holds timer reference so it is retained through configuration changes
 */
@HiltViewModel
class TimerViewModel @Inject constructor(@Named("vm") timer: Timer) : ViewModel(), Timer by timer