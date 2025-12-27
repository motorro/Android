package com.motorro.tasks.app.state

import com.motorro.tasks.USER_NAME
import com.motorro.tasks.app.data.AppData
import com.motorro.tasks.domain.data.User

internal val appData: AppData = AppData(User(USER_NAME))