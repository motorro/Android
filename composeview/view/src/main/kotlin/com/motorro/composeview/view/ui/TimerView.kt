package com.motorro.composeview.view.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.motorro.composeview.appcore.timer.toDisplayString
import com.motorro.composeview.view.R
import com.motorro.composeview.view.databinding.ViewTimerBinding
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.timerViewStyle,
    defStyleRes: Int = R.style.TimerView
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    var title: String by Delegates.observable("") { _, _, newValue ->
        binding.title.text = newValue
    }

    var duration: Duration by Delegates.observable(0.seconds) { _, _, newValue ->
        binding.time.text = newValue.toDisplayString()
    }

    var isRunning: Boolean by Delegates.observable(false) { _, _, newValue ->
        binding.btnStart.isEnabled = newValue.not()
        binding.btnStop.isEnabled = newValue
    }

    private var buttonClickListener: ((Button) -> Unit)? = null
    fun setButtonClickListener(listener: ((Button) -> Unit)?) {
        buttonClickListener = listener
    }

    private val binding = ViewTimerBinding.inflate(LayoutInflater.from(context), this)

    init {
        isSaveEnabled = false
        init(attrs, defStyleAttr, defStyleRes)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val toRetrieve = intArrayOf(
            android.R.attr.layout_width,
            android.R.attr.layout_height,
            R.attr.timer_title,
            R.attr.timer_duration,
            R.attr.timer_running
        )
        context.withStyledAttributes(attrs, toRetrieve, defStyleAttr, defStyleRes) {
            layoutParams = LayoutParams(
                getLayoutDimension(toRetrieve.indexOf(android.R.attr.layout_width), LayoutParams.WRAP_CONTENT),
                getLayoutDimension(toRetrieve.indexOf(android.R.attr.layout_height), LayoutParams.WRAP_CONTENT)
            )
            title = getString(toRetrieve.indexOf(R.attr.timer_title)).orEmpty()
            duration = getString(toRetrieve.indexOf(R.attr.timer_duration))?.let { runCatching { Duration.parse(it) }.getOrNull() } ?: Duration.ZERO
            isRunning = getBoolean(toRetrieve.indexOf(R.attr.timer_running), false)
        }

        binding.time.text = duration.toDisplayString()
        binding.btnStart.isEnabled = isRunning.not()
        binding.btnStop.isEnabled = isRunning

        binding.btnStart.setOnClickListener {
            buttonClickListener?.invoke(Button.BUTTON_START)
        }
        binding.btnStop.setOnClickListener {
            buttonClickListener?.invoke(Button.BUTTON_STOP)
        }
        binding.btnReset.setOnClickListener {
            buttonClickListener?.invoke(Button.BUTTON_RESET)
        }
    }

    companion object {
        enum class Button {
            BUTTON_START,
            BUTTON_STOP,
            BUTTON_RESET
        }
    }
}