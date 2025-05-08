package com.motorro.cookbook.app

import com.motorro.cookbook.app.session.data.Session
import com.motorro.cookbook.data.Profile
import com.motorro.cookbook.data.UserId

val userId = UserId(1)

val profile = Profile(
    id = userId,
    name = "test",
)

val session = Session.Active(
    username = "test",
    password = "password",
    profile = profile,
)