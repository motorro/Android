package com.motorro.notifications.pages.notification.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.notifications.pages.notification.data.NotificationGesture
import com.motorro.notifications.pages.notification.data.NotificationViewState

typealias NotificationState = CommonMachineState<NotificationGesture, NotificationViewState>