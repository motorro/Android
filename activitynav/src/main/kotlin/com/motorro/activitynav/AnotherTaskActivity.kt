package com.motorro.activitynav

import android.content.Context
import android.content.Intent

class AnotherTaskActivity : MainActivity() {
    companion object {
        fun createIntent(context: Context) = Intent(context, AnotherTaskActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        }
    }
}