package com.motorro.notifications.pages.progress.state

import android.content.Context
import androidx.core.app.NotificationCompat

private val CONNECTING = 0 until 10
private val DOWNLOADING = 10 until 60
private val UNZIPPING = 60 until 90
private val SAVING = 90 until 100


fun Context.buildProgressStyle(): NotificationCompat.ProgressStyle = NotificationCompat.ProgressStyle()
    .setProgressPoints(
        listOf(
            NotificationCompat.ProgressStyle.Point(DOWNLOADING.first).setColor(getColor(android.R.color.darker_gray)),
            NotificationCompat.ProgressStyle.Point(UNZIPPING.first).setColor(getColor(android.R.color.darker_gray)),
            NotificationCompat.ProgressStyle.Point(SAVING.first).setColor(getColor(android.R.color.darker_gray)),
        )
    )
    .setProgressSegments(
        listOf(
            NotificationCompat.ProgressStyle.Segment(DOWNLOADING.first),
            NotificationCompat.ProgressStyle.Segment(UNZIPPING.first),
            NotificationCompat.ProgressStyle.Segment(SAVING.first)
        )
    )

