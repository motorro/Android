package com.motorro.architecture.registration.registering

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.appcore.di.ProvidesFragmentContainer
import com.motorro.architecture.appcore.di.containerModel
import com.motorro.architecture.appcore.viewbinding.BindingHost
import com.motorro.architecture.appcore.viewbinding.WithViewBinding
import com.motorro.architecture.appcore.viewbinding.bindView
import com.motorro.architecture.appcore.viewbinding.withBinding
import com.motorro.architecture.core.log.Logging
import com.motorro.architecture.registration.R
import com.motorro.architecture.registration.databinding.FragmentRegisteringBinding
import com.motorro.architecture.registration.registering.data.RegisteringViewState
import com.motorro.architecture.registration.registering.di.buildRegisteringFragmentContainer
import kotlinx.coroutines.launch

internal class RegisteringFragment : Fragment(), ProvidesFragmentContainer, WithViewBinding<FragmentRegisteringBinding> by BindingHost(), Logging {

    override lateinit var fragmentContainer: FragmentContainer

    private val viewModel: RegisteringViewModel by containerModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentContainer = buildRegisteringFragmentContainer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentRegisteringBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            error.setOnDismissListener { _, error ->
                d { "Dismissing error..." }
                if (error.isFatal) {
                    findNavController().navigateUp()
                } else {
                    viewModel.retry()
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                launch {
                    viewModel.viewState.collect { viewState ->
                        when(viewState) {
                            RegisteringViewState.Complete -> {
                                d { "Registration complete" }
                                progress.hide()
                                error.isVisible = false
                                state.isVisible = true
                                state.text = getString(R.string.state_complete)
                            }
                            is RegisteringViewState.Error -> {
                                w(viewState.cause) { "Registration error" }
                                progress.hide()
                                error.isVisible = true
                                error.error = viewState.cause
                                state.isVisible = false
                            }
                            RegisteringViewState.Loading -> {
                                progress.show()
                                error.isVisible = false
                                state.isVisible = false
                            }
                        }
                    }
                }
            }

            error.setOnClickListener {
                d { "Retrying registration..." }
                viewModel.retry()
            }
        }
    }
}