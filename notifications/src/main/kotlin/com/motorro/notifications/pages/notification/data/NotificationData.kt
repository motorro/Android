package com.motorro.notifications.pages.notification.data

import android.os.Parcelable
import com.motorro.notifications.MyNotificationChannel
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationData(
    val title: String = "",
    val text: String = "",
    val channel: MyNotificationChannel = MyNotificationChannel.URGENT
) : Parcelable