package com.motorro.background.pages.service.api

import com.motorro.background.api.MainScreenStateApi
import com.motorro.background.data.MainScreenGesture
import com.motorro.background.pages.service.data.ServiceGesture
import com.motorro.background.pages.service.data.ServiceUiState
import com.motorro.background.pages.service.state.ServiceState
import com.motorro.background.pages.service.state.ServiceStateFactory
import com.motorro.core.log.Logging
import javax.inject.Inject

/**
 * Flow API
 */
class ServiceStateApi @Inject constructor(private val stateFactory: ServiceStateFactory.Impl) : MainScreenStateApi<ServiceGesture, ServiceUiState>, Logging {
    override val data get() = ServicePageData

    override fun init(data: Any?): ServiceState {
        d { "Starting Service flow..." }
        return stateFactory.service()
    }

    override fun getInitialViewState() = ServiceUiState.EMPTY

    override fun mapGesture(parent: MainScreenGesture): ServiceGesture? {
        return when {
            parent is MainScreenGesture.PageGesture && data == parent.page -> parent.gesture as ServiceGesture
            else -> null
        }
    }
}