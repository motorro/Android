package com.motorro.notifications.pages.push.data

data class PushViewState(val fromPush: FromPush? = null)

data class FromPush(
    val title: String? = null,
    val message: String? = null,
    val data: String? = null
)
