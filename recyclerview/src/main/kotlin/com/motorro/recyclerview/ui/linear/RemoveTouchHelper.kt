package com.motorro.recyclerview.ui.linear

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.motorro.recyclerview.ui.linear.data.FlightsDataSource

class RemoveTouchHelper(private val dataSource: FlightsDataSource) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        dataSource.removeAt(viewHolder.bindingAdapterPosition)
    }
}