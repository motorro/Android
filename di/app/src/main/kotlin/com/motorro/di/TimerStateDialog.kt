package com.motorro.di

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.di.databinding.FragmentTimerStateBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.Locale
import kotlin.time.Duration

@AndroidEntryPoint
class TimerStateDialog : DialogFragment(), WithViewBinding<FragmentTimerStateBinding> by BindingHost() {

    private val viewModel: TimerStateViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<TimerStateViewModel.Factory> { factory ->
                factory.create(TimerStateDialogArgs.fromBundle(requireArguments()).providedTime)
            }
        }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentTimerStateBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            supervisorScope {
                launch {
                    viewModel.currentTime.collect {
                        withBinding { timeSystem.text = it.toDisplayString() }
                    }
                }
                launch {
                    viewModel.providedTime.collect {
                        withBinding { timeProvided.text = it.toDisplayString() }
                    }
                }
            }
        }
        withBinding {
            close.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun Duration.toDisplayString(): String {
        val components = this.toComponents { minutes, seconds, nanos -> arrayOf(minutes.toInt(), seconds, nanos / 10_000_000) }

        return String.format(
            Locale.getDefault(),
            "%02d:%02d.%02d",
            *components
        )
    }
}