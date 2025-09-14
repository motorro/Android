package com.motorro.statemachine.statemachine.data

import com.motorro.statemachine.login.data.LoginFlowGesture

/**
 * Login state gesture
 */
data class LoginGesture(val child: LoginFlowGesture) : AppGesture