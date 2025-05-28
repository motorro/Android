package com.motorro.architecture.domain.session

import com.motorro.architecture.domain.session.data.SessionData
import com.motorro.architecture.model.user.UserId
import kotlin.uuid.Uuid

val USER_ID = UserId(Uuid.parse("2f6e2706-f500-44d0-b388-498105e18e77"))

val SESSION_DATA: SessionData = SessionData(
    accessToken = "Access",
    refreshToken = "Refresh",
    userId = USER_ID
)
