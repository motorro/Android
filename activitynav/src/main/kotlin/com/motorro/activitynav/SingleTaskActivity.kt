package com.motorro.activitynav

import android.content.Context
import android.content.Intent

class SingleTaskActivity : MainActivity() {
    companion object {
        fun createIntent(context: Context) = Intent(context, SingleTaskActivity::class.java)
    }
}