package com.motorro.architecture.account.russian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.motorro.architecture.account.russian.data.UiState
import com.motorro.architecture.account.russian.databinding.FragmentRussianBinding
import com.motorro.architecture.account.russian.di.buildRussianFragmentContainer
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.appcore.di.ProvidesFragmentContainer
import com.motorro.architecture.appcore.di.containerModel
import com.motorro.architecture.appcore.viewbinding.BindingHost
import com.motorro.architecture.appcore.viewbinding.WithViewBinding
import com.motorro.architecture.appcore.viewbinding.bindView
import com.motorro.architecture.appcore.viewbinding.withBinding
import com.motorro.architecture.core.log.Logging
import kotlinx.coroutines.launch

class RussianLoginFragment : Fragment(), ProvidesFragmentContainer, WithViewBinding<FragmentRussianBinding> by BindingHost(), Logging {

    override lateinit var fragmentContainer: FragmentContainer

    private val viewModel: RussianViewModel by containerModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentContainer = buildRussianFragmentContainer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindView(container, FragmentRussianBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {

            topAppBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            btnLogin.setOnClickListener {
                viewModel.login()
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.uiState.collect { state ->
                    when(state) {
                        UiState.Prompt -> {
                            d { "Russian prompt" }
                            progress.hide()
                            txtPrompt.isVisible = true
                            welcome.isVisible = false
                            btnLogin.isVisible = true
                            btnLogin.isEnabled = true
                        }
                        UiState.LoggingIn -> {
                            d { "Russian progress" }
                            progress.show()
                            txtPrompt.isVisible = true
                            welcome.isVisible = false
                            btnLogin.isVisible = true
                            btnLogin.isEnabled = false
                        }
                        UiState.Complete -> {
                            d { "Russian progress" }
                            progress.hide()
                            txtPrompt.isVisible = false
                            welcome.isVisible = true
                            btnLogin.isVisible = false
                            btnLogin.isEnabled = false
                        }
                    }
                }
            }
        }
    }
}