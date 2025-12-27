package com.motorro.tasks

import com.motorro.tasks.auth.data.Session
import com.motorro.tasks.data.SessionClaims
import com.motorro.tasks.data.UserName

internal val USER_NAME = UserName("username")

internal val activeSession = Session.Active(
    claims = SessionClaims(
        username = USER_NAME,
        token = "token"
    )
)