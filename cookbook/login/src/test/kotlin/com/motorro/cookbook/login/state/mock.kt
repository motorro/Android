package com.motorro.cookbook.login.state

import com.motorro.cookbook.core.error.UnknownException
import com.motorro.cookbook.login.data.LoginFlowData
import com.motorro.cookbook.model.Profile
import com.motorro.cookbook.model.UserId
import java.io.IOException

internal val DATA: LoginFlowData = LoginFlowData(
    username = "login",
    password = "password"
)

internal val ERROR = UnknownException(IOException("Some error"))

internal val PROFILE = Profile(
    id = UserId(1),
    name = "User"
)