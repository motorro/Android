package com.motorro.tasks.login.state

import com.motorro.tasks.USER_NAME
import com.motorro.tasks.login.data.LoginData
import io.mockk.mockk

internal const val PASSWORD = "password"
internal const val MESSAGE = "Please login!"

internal val loginData: LoginData = LoginData(
    flowHost = mockk(),
    userName = USER_NAME.value,
    message = MESSAGE
)
