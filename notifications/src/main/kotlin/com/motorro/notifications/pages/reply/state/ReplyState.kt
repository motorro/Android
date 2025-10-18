package com.motorro.notifications.pages.reply.state

import androidx.core.app.RemoteInput
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.core.log.Logging
import com.motorro.notifications.data.NotificationAction
import com.motorro.notifications.pages.reply.api.ReplyPageData
import com.motorro.notifications.pages.reply.data.ReplyViewState

class ReplyState(private val action: NotificationAction?) : CommonMachineState<Unit, ReplyViewState>(), Logging {
    override fun doStart() {
        super.doStart()
        setUiState(ReplyViewState(getReply()))
    }

    private fun getReply(): String? = action?.intent?.let {
        RemoteInput.getResultsFromIntent(it)?.getCharSequence(ReplyPageData.REPLY_TEXT_PARAM)?.toString()
    }
}