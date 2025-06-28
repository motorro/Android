package com.motorro.di.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.motorro.di.R
import com.motorro.di.databinding.ViewTimerBinding
import com.motorro.di.timer.Timer
import com.motorro.di.toDisplayString
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.timerViewStyle,
    defStyleRes: Int = R.style.TimerView
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var duration: Duration by Delegates.observable(0.seconds) { _, _, newValue ->
        binding.time.text = newValue.toDisplayString()
    }

    private var isRunning: Boolean by Delegates.observable(false) { _, _, newValue ->
        binding.btnStart.isEnabled = newValue.not()
        binding.btnStop.isEnabled = newValue
    }

    private var timer: Timer? = null
    private var subscription: Job? = null

    fun setTimer(timer: Timer?) {
        subscription?.cancel()
        subscription = null
        this.timer = timer

        if (null != timer) {
            bindTimer()
        }
    }

    private fun bindTimer() = timer?.let { t ->
        val lifecycle = findViewTreeLifecycleOwner() ?: return@let
        binding.title.text = t.title
        subscription = lifecycle.lifecycleScope.launch {
            supervisorScope {
                launch {
                    t.count.onEach { duration = it }.launchIn(this)
                }
                launch {
                    t.isRunning.onEach { isRunning = it }.launchIn(this)
                }
            }
        }
    }

    private val binding = ViewTimerBinding.inflate(LayoutInflater.from(context), this)

    init {
        isSaveEnabled = false
        init(attrs, defStyleAttr, defStyleRes)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val toRetrieve = intArrayOf(
            android.R.attr.layout_width,
            android.R.attr.layout_height
        )
        context.withStyledAttributes(attrs, toRetrieve, defStyleAttr, defStyleRes) {
            layoutParams = LayoutParams(
                getLayoutDimension(toRetrieve.indexOf(android.R.attr.layout_width), LayoutParams.WRAP_CONTENT),
                getLayoutDimension(toRetrieve.indexOf(android.R.attr.layout_height), LayoutParams.WRAP_CONTENT)
            )
        }

        binding.time.text = duration.toDisplayString()
        binding.btnStart.isEnabled = isRunning.not()
        binding.btnStop.isEnabled = isRunning

        binding.btnStart.setOnClickListener {
            timer?.start()
        }
        binding.btnStop.setOnClickListener {
            timer?.stop()
        }
        binding.btnReset.setOnClickListener {
            timer?.reset()
        }

        bindTimer()
    }
}