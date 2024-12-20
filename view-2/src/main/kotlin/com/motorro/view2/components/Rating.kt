package com.motorro.view2.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.content.res.getIntOrThrow
import androidx.core.content.withStyledAttributes
import com.motorro.view2.R
import kotlin.math.ceil

class Rating @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.ratingStyle,
    defStyleRes: Int = R.style.Rating
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var maxRating: Int = 0

    var rating: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    private var stroke: Int = 0
    private lateinit var emptyPaint: Paint
    private lateinit var filledPaint: Paint

    private var listener: ((Int) -> Unit)? = null
    fun setOnRatingChangedListener(listener: (Int) -> Unit) {
        this.listener = listener
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.Rating, defStyleAttr, defStyleRes) {
            maxRating = getIntOrThrow(R.styleable.Rating_maxRating)
            rating = getInt(R.styleable.Rating_rating, rating)
            stroke = getDimensionPixelSizeOrThrow(R.styleable.Rating_strokeWidth)
            emptyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = stroke.toFloat()
                color = getColorOrThrow(R.styleable.Rating_emptyColor)
            }
            filledPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.FILL
                color = getColorOrThrow(R.styleable.Rating_filledColor)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.i(TAG, "onMeasure")
        Log.i(TAG, "Width.size = ${printMeasureSize(widthMeasureSpec)}")
        Log.i(TAG, "Width.mode = ${printMeasureMode(widthMeasureSpec)}")
        Log.i(TAG, "Height.size = ${printMeasureSize(heightMeasureSpec)}")
        Log.i(TAG, "Height.mode = ${printMeasureMode(heightMeasureSpec)}")

        val desiredWidth = maxRating * (dpToPx(SIZE) * 2 + DISTANCE + dpToPx(DISTANCE)) - DISTANCE
        val desiredHeight = dpToPx(SIZE) * 2

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        repeat(maxRating) {
            canvas.drawCircle(
                dpToPx(SIZE + stroke).toFloat(),
                (height / 2).toFloat(),
                dpToPx(SIZE - stroke / 2).toFloat(),
                if (it < rating) filledPaint else emptyPaint
            )
            canvas.translate(dpToPx(SIZE * 2 + DISTANCE).toFloat(), 0f)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val relativeTouch = ceil(event.x / (width/maxRating)).toInt()
                Log.i(TAG, "Touched at: ${event.x}")
                Log.i(TAG, "Touched at relative: $relativeTouch")
                if (rating != relativeTouch) {
                    rating = relativeTouch
                    listener?.invoke(rating)
                }
                return true
            }
        }

        return super.onTouchEvent(event)
    }


    companion object {
        private const val DISTANCE = 8
        private const val SIZE = 24
        private const val TAG = "Rating"
    }
}