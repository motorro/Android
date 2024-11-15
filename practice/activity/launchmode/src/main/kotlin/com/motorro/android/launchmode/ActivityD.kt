package com.motorro.android.launchmode

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.motorro.android.launchmode.databinding.ActivityDBinding

class ActivityD : AppCompatActivity() {

    private lateinit var binding: ActivityDBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupButtons()
        Log.i(TAG, "onCreate: $intent")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.i(TAG, "onNewIntent: $intent")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    private fun setupButtons() = with(binding) {
        openD.setOnClickListener {
            TODO("Send new intent to ActivityD")
        }
        openE.setOnClickListener {
            TODO("Open ActivityE")
        }
    }

    companion object {
        private const val TAG = "ActivityD"
    }
}