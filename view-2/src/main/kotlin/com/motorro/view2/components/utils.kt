package com.motorro.view2.components

import android.view.View

fun View.dpToPx(dp: Int) = (dp * resources.displayMetrics.density).toInt()

fun printMeasureMode(measureSpec: Int) = when(View.MeasureSpec.getMode(measureSpec)) {
    View.MeasureSpec.EXACTLY -> "View.MeasureSpec.EXACTLY"
    View.MeasureSpec.AT_MOST -> "View.MeasureSpec.AT_MOST"
    else -> "View.MeasureSpec.UNSPECIFIED"
}

fun printMeasureSize(measureSpec: Int) = "${View.MeasureSpec.getSize(measureSpec)} px"