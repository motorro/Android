package com.motorro.statemachine.statemachine.data

/**
 * Application gestures
 */
sealed interface AppGesture {
    data object Action : AppGesture
    data object Back: AppGesture
}