package com.motorro.composeview.view.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motorro.composeview.appcore.timer.model.TimerGesture
import com.motorro.composeview.appcore.timer.model.TimerViewState
import com.motorro.composeview.view.databinding.VhTimerBinding
import com.motorro.composeview.view.ui.TimerView.Companion.Button.BUTTON_RESET
import com.motorro.composeview.view.ui.TimerView.Companion.Button.BUTTON_START
import com.motorro.composeview.view.ui.TimerView.Companion.Button.BUTTON_STOP
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.withBinding


class TimerAdapter(private val callback: Callback) : ListAdapter<TimerViewState, TimerAdapter.ViewHolder>(TimerViewStateDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(VhTimerBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface Callback {
        fun onGesture(timerId: Int, gesture: TimerGesture)
    }

    inner class ViewHolder(override var binding: VhTimerBinding?) : RecyclerView.ViewHolder(requireNotNull(binding).root), WithViewBinding<VhTimerBinding> {

        private var id: Int = -1

        init {
            withBinding {
                root.setButtonClickListener {
                    check(id >= 0) {
                        "Click from uninitialized view-holder"
                    }
                    callback.onGesture(id, when(it) {
                        BUTTON_START -> TimerGesture.StartPressed
                        BUTTON_STOP -> TimerGesture.StopPressed
                        BUTTON_RESET -> TimerGesture.ResetPressed
                    })
                }
            }
        }

        fun bind(state: TimerViewState) = withBinding {
            this@ViewHolder.id = state.id
            root.title = state.title
            root.duration = state.count
            root.isRunning = state.isRunning
        }
    }
}

object TimerViewStateDiffCallback : DiffUtil.ItemCallback<TimerViewState>() {
    override fun areItemsTheSame(oldItem: TimerViewState, newItem: TimerViewState): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: TimerViewState, newItem: TimerViewState): Boolean {
        return oldItem == newItem
    }
}