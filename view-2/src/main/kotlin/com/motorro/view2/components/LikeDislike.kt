package com.motorro.view2.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import com.motorro.view2.R
import com.motorro.view2.databinding.LikeDislikeBinding

class LikeDislike @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.likeDislikePanelStyle,
    defStyleRes: Int = R.style.LikeDislike
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val binding = LikeDislikeBinding.inflate(LayoutInflater.from(context), this)
    private var likes = 0

    init {
        initPanel(attrs, defStyleAttr, defStyleRes)
        initLikes()
    }

    private fun initPanel(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val toRetrieve = intArrayOf(
            android.R.attr.background
        )
        context.withStyledAttributes(attrs, toRetrieve, defStyleAttr, defStyleRes) {
            background = getDrawable(toRetrieve.indexOf(android.R.attr.background))
        }

        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL
        gravity = CENTER
        setPadding(
            0,
            dpToPx(8),
            0,
            dpToPx(8)
        )
    }

    private fun initLikes() = with(binding) {
        updateLikes()
        likeButton.setOnClickListener {
            ++likes
            updateLikes()
        }
        dislikeButton.setOnClickListener {
            if (likes > 0) {
                --likes
            }
            updateLikes()
        }
    }

    private fun updateLikes() = with(binding) {
        numLikes.text = context.getString(R.string.likes, likes)
    }
}