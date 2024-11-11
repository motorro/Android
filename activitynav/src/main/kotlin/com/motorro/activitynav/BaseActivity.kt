package com.motorro.activitynav

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    lateinit var activityId: ActivityId
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        activityId = ActivityId.create(this)
        super.onCreate(savedInstanceState)
    }
}