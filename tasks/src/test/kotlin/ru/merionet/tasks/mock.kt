package ru.merionet.tasks

import ru.merionet.tasks.auth.data.Session
import ru.merionet.tasks.data.SessionClaims
import ru.merionet.tasks.data.UserName

internal val USER_NAME = UserName("username")

internal val activeSession = Session.Active(
    claims = SessionClaims(
        username = USER_NAME,
        token = "token"
    )
)