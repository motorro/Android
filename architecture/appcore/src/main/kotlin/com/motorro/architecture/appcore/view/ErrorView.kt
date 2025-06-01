package com.motorro.architecture.appcore.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.motorro.architecture.appcore.R
import com.motorro.architecture.appcore.databinding.LayoutErrorBinding
import com.motorro.architecture.core.error.CoreException
import kotlin.properties.Delegates

/**
 * Common error view
 */
class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.errorViewStyle,
    defStyleRes: Int = R.style.ErrorView
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = LayoutErrorBinding.inflate(LayoutInflater.from(context), this)

    /**
     * Error to display
     */
    var error: CoreException? by Delegates.observable(null) { _, _, value ->
        with(binding) {
            textError.text = error?.message.orEmpty()
            btnDismiss.text = if (true == error?.isFatal) {
                context.getText(R.string.btn_close)
            } else {
                context.getText(R.string.btn_retry)
            }
        }
    }

    /**
     * Sets on-dismiss listener
     */
    fun setOnDismissListener(listener: ((ErrorView, CoreException) -> Unit)?) {
        if (null == listener) {
            binding.btnDismiss.setOnClickListener(null)
        } else {
            binding.btnDismiss.setOnClickListener {
                val currentError = error
                if (null != currentError) {
                    listener(this, currentError)
                }
            }
        }
    }

    init {
        isSaveEnabled = false
        init(attrs, defStyleAttr, defStyleRes)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val toRetrieve = intArrayOf(
            R.attr.icon,
            android.R.attr.layout_width,
            android.R.attr.layout_height
        )
        context.withStyledAttributes(attrs, toRetrieve, defStyleAttr, defStyleRes) {
            binding.imageError.setImageDrawable(getDrawable(toRetrieve.indexOf(R.attr.icon)))
            layoutParams = LayoutParams(
                getLayoutDimension(toRetrieve.indexOf(android.R.attr.layout_width), LayoutParams.WRAP_CONTENT),
                getLayoutDimension(toRetrieve.indexOf(android.R.attr.layout_height), LayoutParams.WRAP_CONTENT)
            )
        }
    }
}