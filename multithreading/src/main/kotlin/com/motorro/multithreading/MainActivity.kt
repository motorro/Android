package com.motorro.multithreading

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.motorro.multithreading.databinding.ActivityMainBinding
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var th: BgThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            start.setOnClickListener {
                start.isEnabled = false
                send.isEnabled = true
                startThread()
            }
            send.setOnClickListener {
                sendMessage()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        th?.terminate()
    }

    private fun startThread() {
        th = BgThread().apply { start() }
    }

    private fun sendMessage() {
        th?.post {
            log { "Current time: ${Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time}" }
        }
    }
}