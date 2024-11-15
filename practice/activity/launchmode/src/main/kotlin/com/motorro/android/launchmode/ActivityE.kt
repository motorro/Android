package com.motorro.android.launchmode

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.motorro.android.launchmode.databinding.ActivityEBinding

class ActivityE : AppCompatActivity() {

    private lateinit var binding: ActivityEBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupButtons()
        Log.i(TAG, "onCreate: $intent")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    private fun setupButtons() = with(binding) {
        openD.setOnClickListener {
            TODO("Open ActivityD in the same task")
        }
        returnToC.setOnClickListener {
            TODO("Return to ActivityC clearing all activities on top of it and sending new intent")
        }
    }

    companion object {
        private const val TAG = "ActivityE"
    }
}