package ru.merionet.tasks.login.state

import io.mockk.mockk
import ru.merionet.tasks.USER_NAME
import ru.merionet.tasks.login.data.LoginData

internal const val PASSWORD = "password"
internal const val MESSAGE = "Please login!"

internal val loginData: LoginData = LoginData(
    flowHost = mockk(),
    userName = USER_NAME,
    message = MESSAGE
)
