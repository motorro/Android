package com.motorro.notifications.pages.progress.state

import androidx.core.app.NotificationCompat
import com.motorro.notifications.MyNotificationChannel
import com.motorro.notifications.R
import com.motorro.notifications.pages.progress.data.ProgressGesture
import com.motorro.notifications.pages.progress.data.ProgressViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

private val CONNECTING = 10
private val DOWNLOADING = 60
private val UNZIPPING = 90
private val SAVING = 100

class PlayingState(context: ProgressContext, progress: Int) : BaseProgressState(context) {

    private var progress by Delegates.observable(progress) { _, _, newValue -> render(newValue) }

    override fun doStart() {
        super.doStart()
        render(progress)
        count()
    }

    fun count() = stateScope.launch {
        while (progress < 100) {
            delay(200)
            progress++
        }
        setMachineState(factory.stopped())
    }

    override fun doProcess(gesture: ProgressGesture) {
        when(gesture) {
            ProgressGesture.Stop -> {
                d { "Stopping..." }
                setMachineState(factory.stopped())
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun render(progress: Int) {
        setUiState(
            ProgressViewState.Player.InProgress(
                progress = progress,
                phaseName = getProgressPhaseName(progress)
            )
        )
        notify(progress)
    }

    private fun notify(progress: Int) {
        val notification = NotificationCompat.Builder(androidContext, MyNotificationChannel.MEDIUM.name)
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .setRequestPromotedOngoing(true)
            .setContentTitle(androidContext.getString(R.string.lbl_download_progress))
            .setContentText(getProgressPhaseName(progress))
            .setStyle(buildProgressStyle(progress))
            .build()

        notify(notification)
    }

    private fun getProgressPhaseName(progress: Int): String  = when(progress) {
        in 0 until CONNECTING -> androidContext.getString(R.string.lbl_connecting)
        in CONNECTING until DOWNLOADING -> androidContext.getString(R.string.lbl_downloading)
        in DOWNLOADING .. UNZIPPING -> androidContext.getString(R.string.lbl_unzipping)
        else -> androidContext.getString(R.string.lbl_saving)
    }

    private fun buildProgressStyle(progress: Int) = NotificationCompat.ProgressStyle()
        .setProgressIndeterminate(false)
        .setProgressPoints(
            listOf(
                NotificationCompat.ProgressStyle.Point(CONNECTING),
                NotificationCompat.ProgressStyle.Point(DOWNLOADING),
                NotificationCompat.ProgressStyle.Point(UNZIPPING),
                NotificationCompat.ProgressStyle.Point(SAVING),
            )
        )
        .setProgressSegments(
            listOf(
                NotificationCompat.ProgressStyle.Segment(CONNECTING - 0),
                NotificationCompat.ProgressStyle.Segment(DOWNLOADING - CONNECTING),
                NotificationCompat.ProgressStyle.Segment(UNZIPPING - DOWNLOADING),
                NotificationCompat.ProgressStyle.Segment(SAVING - UNZIPPING)
            )
        )
        .setProgress(progress)

    override fun doClear() {
        super.doClear()
        cancelNotification()
    }
}