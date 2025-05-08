package com.motorro.cookbook.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.motorro.cookbook.app.databinding.FragmentLoginBinding
import com.motorro.cookbook.app.session.data.LoginViewState
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment(), WithViewBinding<FragmentLoginBinding> by BindingHost() {

    private val model: LoginViewModel by viewModels {
        LoginViewModel.Factory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(
        container,
        FragmentLoginBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            username.doAfterTextChanged {
                model.setLogin(it.toString())
            }
            password.doAfterTextChanged {
                model.setPassword(it.toString())
            }
            login.setOnClickListener {
                model.login()
            }
        }

        fun setControlsState(state: LoginViewState) = withBinding {
            username.isEnabled = state.controlsEnabled
            password.isEnabled = state.controlsEnabled
            login.isEnabled = state.loginEnabled
            error.isVisible = state is LoginViewState.Error
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.viewState.collect { state -> withBinding {
                    setControlsState(state)
                    when(state) {
                        is LoginViewState.Form -> {
                            progress.hide()
                            username.setTextKeepState(state.username)
                            password.setTextKeepState(state.password)
                        }
                        is LoginViewState.Error -> {
                            progress.hide()
                            username.setTextKeepState(state.username)
                            password.setTextKeepState(state.password)
                            errorText.text = state.message
                        }
                        is LoginViewState.Loading -> {
                            progress.show()
                            username.setTextKeepState(state.username)
                            password.setTextKeepState(state.password)
                        }
                        LoginViewState.LoggedIn -> {
                            findNavController().popBackStack()
                        }
                    }
                }}
            }
        }
    }
}