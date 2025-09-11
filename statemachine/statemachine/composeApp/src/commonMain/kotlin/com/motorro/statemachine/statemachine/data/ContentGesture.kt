package com.motorro.statemachine.statemachine.data

/**
 * Content state gesture
 */
interface ContentGesture : AppGesture {
    /**
     * Logout
     */
    data object Logout : ContentGesture
}