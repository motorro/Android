package com.motorro.android.launchmode

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.motorro.android.launchmode.databinding.ActivityCBinding

class ActivityC : AppCompatActivity() {

    private lateinit var binding: ActivityCBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCBinding.inflate(layoutInflater)
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
        openA.setOnClickListener {
            TODO("Open ActivityA clearing back stack on top of it")
        }
        openD.setOnClickListener {
            TODO("Open ActivityD")
        }
    }

    companion object {
        private const val TAG = "ActivityC"
    }
}