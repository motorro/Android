package com.motorro.cookbook.recipedemo.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.cookbook.appcore.navigation.auth.AuthGesture
import com.motorro.cookbook.appcore.navigation.auth.AuthViewState
import com.motorro.cookbook.appcore.navigation.auth.AuthenticationApi
import com.motorro.cookbook.model.Profile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Named("Application")
    fun applicationScope(): CoroutineScope = MainScope()

    @Provides
    fun authenticationApi(): AuthenticationApi = object : AuthenticationApi {

        private fun reject(): Nothing = throw NotImplementedError("Authentication not implemented")

        override fun <PG : Any, PU : Any> createProxy(
            onLoginFactory: (Profile) -> CommonMachineState<PG, PU>,
            onCancelFactory: () -> CommonMachineState<PG, PU>,
            mapGesture: (PG) -> AuthGesture?,
            mapUiState: (AuthViewState) -> PU
        ): CommonMachineState<PG, PU> = reject()

        @Composable
        override fun AuthenticationScreen(
            state: AuthViewState,
            onGesture: (AuthGesture) -> Unit,
            modifier: Modifier
        ) {
            reject()
        }
    }
}