package com.motorro.activity.ui

import androidx.recyclerview.widget.RecyclerView

/**
 * Adds margin to recycler view items
 */
class MarginDecorator(private val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: android.graphics.Rect, view: android.view.View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = margin
            }
            left = margin
            right = margin
            bottom = margin
        }
    }
}