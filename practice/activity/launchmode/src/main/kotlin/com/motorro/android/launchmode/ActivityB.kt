package com.motorro.android.launchmode

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.motorro.android.launchmode.databinding.ActivityBBinding

class ActivityB : AppCompatActivity() {

    private lateinit var binding: ActivityBBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupButtons()
        Log.i(TAG, "onCreate: $intent")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    private fun setupButtons() = with(binding) {
        openC.setOnClickListener {
            TODO("Open ActivityC in new task")
        }
        close.setOnClickListener {
            TODO("Close this activity")
        }
    }

    companion object {
        private const val TAG = "ActivityB"
    }
}