package com.motorro.notifications.pages.progress.data

sealed class ProgressGesture {
    data object RecheckPromo: ProgressGesture()
    data object SkipPromo : ProgressGesture()
    data object Play : ProgressGesture()
    data object Stop : ProgressGesture()
}