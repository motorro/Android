package com.motorro.activitynav

import android.content.Context
import android.content.Intent

class SingleInstanceActivity : MainActivity() {
    companion object {
        fun createIntent(context: Context) = Intent(context, SingleInstanceActivity::class.java)
    }
}