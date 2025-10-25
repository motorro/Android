package com.motorro.notifications.pages.push.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.core.log.Logging
import com.motorro.notifications.data.NotificationAction
import com.motorro.notifications.pages.push.api.PushPageData
import com.motorro.notifications.pages.push.data.FromPush
import com.motorro.notifications.pages.push.data.PushViewState

class PushState(private val action: NotificationAction?) : CommonMachineState<Unit, PushViewState>(), Logging {
    override fun doStart() {
        super.doStart()
        setUiState(PushViewState(getReply()))
    }

    private fun getReply(): FromPush? = action?.intent?.let {
        FromPush(
            title = it.getStringExtra(PushPageData.PUSH_TITLE_PARAM),
            message = it.getStringExtra(PushPageData.PUSH_MESSAGE_PARAM),
            data = it.getStringExtra(PushPageData.PUSH_DATA_PARAM)
        )
    }
}