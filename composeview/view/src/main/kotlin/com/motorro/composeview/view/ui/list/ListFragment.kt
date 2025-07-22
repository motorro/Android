package com.motorro.composeview.view.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.motorro.composeview.appcore.timer.model.ListViewModel
import com.motorro.composeview.appcore.timer.model.TimerGesture
import com.motorro.composeview.view.databinding.FragmentListBinding
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment(), WithViewBinding<FragmentListBinding> by BindingHost() {
    private val viewModel: ListViewModel by viewModels()
    private val adapter = TimerAdapter(object : TimerAdapter.Callback {
        override fun onGesture(timerId: Int, gesture: TimerGesture) {
            viewModel.process(timerId, gesture)
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentListBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewStates.collect(adapter::submitList)
        }
    }

    private fun setupList() = withBinding {
        timers.adapter = adapter
        timers.itemAnimator = null
        timers.addItemDecoration(DividerItemDecoration(
            timers.getContext(),
            checkNotNull(timers.layoutManager).layoutDirection
        ))
    }
}