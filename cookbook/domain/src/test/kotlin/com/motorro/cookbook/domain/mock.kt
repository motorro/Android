package com.motorro.cookbook.domain

import com.motorro.cookbook.domain.session.data.Session
import com.motorro.cookbook.model.Profile
import com.motorro.cookbook.model.UserId

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