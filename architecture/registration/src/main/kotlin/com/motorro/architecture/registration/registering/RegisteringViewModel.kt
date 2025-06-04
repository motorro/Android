package com.motorro.architecture.registration.registering

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.core.log.Logging
import com.motorro.architecture.domain.profile.usecase.UpdateCurrentUserProfileUsecase
import com.motorro.architecture.model.user.CountryCode
import com.motorro.architecture.model.user.NewUserProfile
import com.motorro.architecture.registration.data.RegistrationData
import com.motorro.architecture.registration.registering.data.RegisteringViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Updates user profile
 */
internal class RegisteringViewModel(
    private val registrationData: RegistrationData,
    private val updateUserProfileUsecase: UpdateCurrentUserProfileUsecase
) : ViewModel(), Logging {

    private val collector: MutableStateFlow<RegisteringViewState> = MutableStateFlow(RegisteringViewState.Loading)
    private var job: Job? = null

    init {
        register()
    }

    /**
     * Checks data and runs registration
     */
    private fun register() {
        job?.cancel()
        val name: String = registrationData.name.value.value
        val country: CountryCode = registrationData.country.value.value

        job = viewModelScope.launch {
            updateUserProfileUsecase(NewUserProfile(name, country))
                .map {
                    when(it) {
                        is LceState.Content -> RegisteringViewState.Complete
                        is LceState.Error -> RegisteringViewState.Error(it.error)
                        is LceState.Loading -> RegisteringViewState.Loading
                    }
                }
                .collect(collector)
        }
    }

    /**
     * View state
     */
    val viewState: StateFlow<RegisteringViewState> get() = collector.asStateFlow()

    /**
     * Retries error
     */
    fun retry() {
        register()
    }
}