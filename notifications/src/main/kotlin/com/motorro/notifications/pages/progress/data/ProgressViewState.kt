package com.motorro.notifications.pages.progress.data

sealed class ProgressViewState {
    /**
     * Promo settings
     */
    data object PromoSettings : ProgressViewState()

    sealed class Player : ProgressViewState() {
        /**
         * Stopped
         */
        data object Idle : Player()

        /**
         * Playing
         */
        data class InProgress(val progress: Int, val phaseName: String) : Player()
    }
}
