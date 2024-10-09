package ru.merionet.tasks.login.state

import ru.merionet.tasks.login.data.LoginData


internal const val USER_NAME = "username"
internal const val PASSWORD = "password"
internal const val MESSAGE = "Please login!"

internal val loginData: LoginData = LoginData(userName = USER_NAME, message = MESSAGE)