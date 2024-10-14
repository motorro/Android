package ru.merionet.tasks.app.state

import ru.merionet.tasks.USER_NAME
import ru.merionet.tasks.app.data.AppData
import ru.merionet.tasks.domain.data.User

internal val appData: AppData = AppData(User(USER_NAME))