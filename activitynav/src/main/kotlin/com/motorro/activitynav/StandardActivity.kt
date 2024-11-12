package com.motorro.activitynav

import android.content.Context
import android.content.Intent

class StandardActivity : MainActivity() {
    companion object {
        fun createIntent(context: Context) = Intent(context, StandardActivity::class.java)
    }
}