package com.motorro.lifecycle

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.motorro.core.log.Logging
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : AppCompatActivity(), Logging {

    companion object {
        /**
         * Instance counter
         */
        private var instanceCounter = 0

        private const val STATE_CREATED = "created"
        private const val STATE_STARTED = "started"
        private const val STATE_RESUMED = "resumed"

        /**
         * Reading duration from bundle
         */
        private fun Bundle.getDuration(key: String): Duration {
            return getLong(key).milliseconds
        }

        /**
         * Writing duration to bundle
         */
        private fun Bundle.putDuration(key: String, value: Duration) {
            putLong(key, value.inWholeMilliseconds)
        }
    }

    private lateinit var createdTimer: Timer
    private lateinit var startedTimer: Timer
    private lateinit var resumedTimer: Timer

    init {
        logCurrentState(this, "init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        i { "onCreate" }
        logCurrentState(this, "onCreate")

        // Increment instance counter
        findViewById<TextView>(R.id.instanceCounter).text = "${++instanceCounter}"

        createdTimer = Timer(
            lifecycleScope,
            updateTimerView(R.id.timeCreated),
            savedInstanceState?.getDuration(STATE_CREATED)
        )
        createdTimer.start()

        startedTimer = Timer(
            lifecycleScope,
            updateTimerView(R.id.timeStarted),
            savedInstanceState?.getDuration(STATE_STARTED)
        )
        resumedTimer = Timer(
            lifecycleScope,
            updateTimerView(R.id.timeResumed),
            savedInstanceState?.getDuration(STATE_RESUMED)
        )

        // Initialize view components
        findViewById<Button>(R.id.startOpaque).setOnClickListener {
            startActivity(OpaqueActivity.newIntent(this))
        }
        findViewById<Button>(R.id.startTranslucent).setOnClickListener {
            startActivity(TranslucentActivity.newIntent(this))
        }
        findViewById<Button>(R.id.close).setOnClickListener {
            finish()
        }
    }

    /**
     * Saves current data state
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putDuration(STATE_CREATED, createdTimer.time)
        outState.putDuration(STATE_STARTED, startedTimer.time)
        outState.putDuration(STATE_RESUMED, resumedTimer.time)
    }

    override fun onStart() {
        super.onStart()

        i { "onStart" }
        logCurrentState(this, "onStart")

        startedTimer.start()
    }

    override fun onResume() {
        super.onResume()

        i { "onResume" }
        logCurrentState(this, "onResume")

        resumedTimer.start()
    }

    override fun onPause() {
        super.onPause()

        w { "onPause at ${resumedTimer.time.format()}" }
        logCurrentState(this, "onPause")

        resumedTimer.stop()
    }

    override fun onStop() {
        super.onStop()

        w { "onStop at ${startedTimer.time.format()}" }
        logCurrentState(this, "onStop")

        startedTimer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()

        e { "onDestroy at ${createdTimer.time.format()}" }
        logCurrentState(this, "onDestroy")

        createdTimer.stop()
    }

    private fun updateTimerView(@IdRes viewId: Int): (Timer.State) -> Unit {
        val text = findViewById<TextView>(viewId)
        return {
            text.text = it.time.format()
            text.setTextColor(if (it.running) getColor(R.color.green) else getColor(R.color.red))
        }
    }
}