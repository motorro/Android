package com.motorro.composeview.view.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.motorro.composeview.view.databinding.FragmentTimerBinding
import com.motorro.composeview.view.ui.TimerView.Companion.Button
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TimerFragment : Fragment(), WithViewBinding<FragmentTimerBinding> by BindingHost() {
    private val viewModel: TimerViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentTimerBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            timer1.title = viewModel.title1
            timer2.title = viewModel.title2

            timer1.setButtonClickListener {
                when(it) {
                    Button.BUTTON_START -> viewModel.start1()
                    Button.BUTTON_STOP -> viewModel.stop1()
                    Button.BUTTON_RESET -> viewModel.reset1()
                }
            }
            timer2.setButtonClickListener {
                when(it) {
                    Button.BUTTON_START -> viewModel.start2()
                    Button.BUTTON_STOP -> viewModel.stop2()
                    Button.BUTTON_RESET -> viewModel.reset2()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.count1.collect {
                    withBinding {
                        timer1.duration = it
                    }
                }
            }
            launch {
                viewModel.isRunning1.collect {
                    withBinding {
                        timer1.isRunning = it
                    }
                }
            }
            launch {
                viewModel.count2.collect {
                    withBinding {
                        timer2.duration = it
                    }
                }
            }
            launch {
                viewModel.isRunning2.collect {
                    withBinding {
                        timer2.isRunning = it
                    }
                }
            }
        }
    }
}