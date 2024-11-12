package com.motorro.activitynav

import android.content.Context
import android.content.Intent

class SingleTopActivity : MainActivity() {
    companion object {
        fun createIntent(context: Context) = Intent(context, SingleTopActivity::class.java)
    }
}