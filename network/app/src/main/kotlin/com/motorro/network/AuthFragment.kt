package com.motorro.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.network.databinding.FragmentAuthBinding
import kotlinx.coroutines.launch

class AuthFragment : DialogFragment(), WithViewBinding<FragmentAuthBinding> by BindingHost() {
    private val model: AuthFragmentModel by viewModels {
        AuthFragmentModel.Factory(app())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentAuthBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            viewLifecycleOwner.lifecycle.coroutineScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        model.token.collect { t ->
                            token.setTextKeepState(t.orEmpty())
                        }
                    }
                    launch {
                        model.saveEnabled.collect { enabled ->
                            save.isEnabled = enabled
                        }
                    }
                }
            }

            token.doAfterTextChanged {
                model.updateToken(it.toString())
            }

            save.setOnClickListener {
                model.save()
                dismiss()
            }
            cancel.setOnClickListener {
                dismiss()
            }
        }
    }
}