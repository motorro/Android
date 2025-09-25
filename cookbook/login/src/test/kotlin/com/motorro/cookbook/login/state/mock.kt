package com.motorro.cookbook.login.state

import com.motorro.cookbook.core.error.UnknownException
import com.motorro.cookbook.login.data.LoginFlowData
import java.io.IOException

internal val DATA: LoginFlowData = LoginFlowData(
    username = "login",
    password = "password"
)

internal val ERROR = UnknownException(IOException("Some error"))
