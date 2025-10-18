package com.motorro.notifications.pages.progress.state

import com.motorro.notifications.pages.progress.data.ProgressGesture
import com.motorro.notifications.pages.progress.data.ProgressViewState

/**
 * Checks API 36 Promoted notifications permissions
 */
class CheckingPromoState(context: ProgressContext) : BaseProgressState(context) {
    override fun doStart() {
        super.doStart()
        checkIfPromoEnabled()
    }

    // Uncomment if you want to try API 36 Promoted Notifications
    fun shouldCheckForPromo() = false // Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA

    private fun checkIfPromoEnabled() {
        when {
            shouldCheckForPromo().not() -> {
                i { "Skipping promo check" }
                setMachineState(factory.stopped())
            }
            notificationManager.canPostPromotedNotifications() -> {
                i { "Promoted notifications are enabled" }
                setMachineState(factory.stopped())
            }
            else -> {
                i { "Promoted notifications are not enabled" }
                setUiState(ProgressViewState.PromoSettings)
            }
        }
    }

    override fun doProcess(gesture: ProgressGesture) {
        when(gesture) {
            ProgressGesture.RecheckPromo -> checkIfPromoEnabled()
            ProgressGesture.SkipPromo -> {
                i { "Skipping promoted status..." }
                setMachineState(factory.stopped())
            }
            else -> super.doProcess(gesture)
        }
    }
}