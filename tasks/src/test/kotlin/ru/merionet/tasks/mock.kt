package ru.merionet.tasks

import ru.merionet.tasks.auth.data.Session
import ru.merionet.tasks.data.SessionClaims

internal const val USER_NAME = "username"

internal val activeSession = Session.Active(
    claims = SessionClaims(
        username = USER_NAME,
        token = "token"
    )
)