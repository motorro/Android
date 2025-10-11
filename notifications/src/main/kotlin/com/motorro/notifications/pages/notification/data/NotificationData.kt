package com.motorro.notifications.pages.notification.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationData(
    val title: String = "",
    val text: String = ""
) : Parcelable