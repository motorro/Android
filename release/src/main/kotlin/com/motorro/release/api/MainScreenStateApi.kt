package com.motorro.release.api

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.release.data.MainScreenGesture

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
    fun init(data: Any?) : CommonMachineState<CG, CU>

    /**
     * Initial view state
     */
    fun getInitialViewState(): CU

    /**
     * Maps gesture to child
     */
    fun mapGesture(parent: MainScreenGesture): CG?
}
