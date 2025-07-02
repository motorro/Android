package com.motorro.cookbook.data.session

import com.motorro.cookbook.domain.session.data.Session
import com.motorro.cookbook.model.Profile
import com.motorro.cookbook.model.UserId

val USER_ID = UserId(1)

val PROFILE = Profile(
    id = USER_ID,
    name = "test",
)

val SESSION = Session.Active(
    username = "test",
    password = "password",
    profile = PROFILE,
)