package com.motorro.cookbook.login.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.cookbook.appcore.navigation.auth.AuthFlowHost
import com.motorro.cookbook.appcore.navigation.auth.AuthGesture
import com.motorro.cookbook.appcore.navigation.auth.AuthViewState
import com.motorro.cookbook.appcore.navigation.auth.AuthenticationApi
import com.motorro.cookbook.core.log.Logging
import com.motorro.cookbook.login.LoginScreen
import com.motorro.cookbook.login.data.LoginGesture
import com.motorro.cookbook.login.data.LoginViewState
import com.motorro.cookbook.login.state.LoginStateFactory
import com.motorro.cookbook.model.Profile
import javax.inject.Inject

/**
 * Login API implementation
 */
internal class LoginAuthenticationApi @Inject constructor(private val factory: LoginStateFactory.Impl.Factory) : AuthenticationApi {

    override fun <PG : Any, PU : Any> createProxy(
        onLoginFactory: (Profile) -> CommonMachineState<PG, PU>,
        onCancelFactory: () -> CommonMachineState<PG, PU>,
        mapGesture: (PG) -> AuthGesture?,
        mapUiState: (AuthViewState) -> PU
    ): CommonMachineState<PG, PU> = object : ProxyMachineState<PG, PU, LoginGesture, LoginViewState>(LoginViewState.EMPTY), Logging {

        private val authProxy = object : AuthFlowHost {
            override fun onLogin(profile: Profile) {
                d { "Login successful: $profile" }
                setMachineState(onLoginFactory(profile))
            }

            override fun onCancel() {
                d { "Login cancelled" }
                setMachineState(onCancelFactory())
            }
        }

        override fun doStart() {
            d { "Starting login proxy..." }
            super.doStart()
        }

        override fun init() = factory.create(authProxy).init()

        override fun mapGesture(parent: PG): LoginGesture? = requireNotNull(mapGesture(parent) as? LoginGesture) {
            "LoginGesture expected, but $parent found"
        }

        override fun mapUiState(child: LoginViewState): PU = mapUiState(child)

        override fun doClear() {
            super.doClear()
            d { "Login proxy cleared" }
        }
    }

    @Composable
    override fun AuthenticationScreen(
        state: AuthViewState,
        onGesture: (AuthGesture) -> Unit,
        modifier: Modifier
    ) {
        val loginState = requireNotNull(state as? LoginViewState) {
            "LoginViewState expected, but $state found"
        }
        LoginScreen(loginState, onGesture, modifier)
    }
}