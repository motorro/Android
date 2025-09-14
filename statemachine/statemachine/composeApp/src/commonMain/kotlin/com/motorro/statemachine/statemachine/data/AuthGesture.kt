package com.motorro.statemachine.statemachine.data

import com.motorro.statemachine.auth.data.AuthFlowGesture

/**
 * Auth state gesture
 */
data class AuthGesture(val child: AuthFlowGesture) : AppGesture