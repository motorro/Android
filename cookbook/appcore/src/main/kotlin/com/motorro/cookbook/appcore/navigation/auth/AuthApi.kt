package com.motorro.cookbook.appcore.navigation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.cookbook.model.Profile

/**
 * Marker for any authentication-flow gestures
 */
interface AuthGesture

/**
 * Marker for any authentication-flow ui-states
 */
interface AuthViewState

/**
 * Auth module API
 */
interface AuthenticationApi {
    /**
     * Creates proxy for authentication flow
     * @param onLoginFactory Called when login is successful and switches to returned state
     * @param onCancelFactory Called when login is cancelled and switches to returned state
     * @return Authentication proxy
     */
    fun <PG: Any, PU: Any> createProxy(
        onLoginFactory: (Profile) -> CommonMachineState<PG, PU>,
        onCancelFactory: () -> CommonMachineState<PG, PU>,
        mapGesture: (parent: PG) -> AuthGesture?,
        mapUiState: (child: AuthViewState) -> PU
    ): CommonMachineState<PG, PU>

    /**
     * Authentication screen composable
     */
    @Composable
    fun AuthenticationScreen(
        state: AuthViewState,
        onGesture: (AuthGesture) -> Unit,
        modifier: Modifier = Modifier
    )
}

/**
 * Local for [AuthenticationApi]
 */
val LocalAuthenticationApi = staticCompositionLocalOf<AuthenticationApi> {
    error("No AuthenticationApi provided")
}

/**
 * Provides local glide dependencies
 */
@Composable
fun WithLocalAuthentication(api: AuthenticationApi, block: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAuthenticationApi provides api) {
        block()
    }
}