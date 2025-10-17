package com.motorro.notifications.api

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.notifications.data.MainScreenGesture
import com.motorro.notifications.data.NotificationAction

/**
 * Main screen proxy state API
 */
interface MainScreenStateApi<CG: Any, CU: Any> {
    /**
     * Page data
     */
    val data: MainScreenPageData

    /**
     * Initializes page
     */
    fun init(data: NotificationAction?) : CommonMachineState<CG, CU>

    /**
     * Initial view state
     */
    fun getInitialViewState(): CU

    /**
     * Maps gesture to child
     */
    fun mapGesture(parent: MainScreenGesture): CG?
}
