package com.motorro.view2.components

import android.view.View

fun View.dpToPx(dp: Int) = (dp * resources.displayMetrics.density).toInt()