package com.motorro.tasks

import com.motorro.tasks.auth.data.Session
import com.motorro.tasks.data.SessionClaims

internal const val USER_NAME = "username"

internal val activeSession = Session.Active(
    claims = SessionClaims(
        username = USER_NAME,
        token = "token"
    )
)