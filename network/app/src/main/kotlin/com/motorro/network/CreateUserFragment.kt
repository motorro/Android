package com.motorro.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.network.data.CreateUserData
import com.motorro.network.databinding.FragmentCreateBinding

class CreateUserFragment : DialogFragment(), WithViewBinding<FragmentCreateBinding> by BindingHost() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentCreateBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            name.doAfterTextChanged {
                save.isEnabled = name.text.isNullOrEmpty().not()
            }

            save.setOnClickListener {
                setResult(name.text.toString())
            }
            cancel.setOnClickListener {
                setResult(null)
            }
        }
    }

    private fun setResult(name: String?) {
        if (null != name) {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                CREATE_RESULT,
                CreateUserData(name)
            )
        }
        findNavController().navigateUp()
    }

    companion object {
        const val CREATE_RESULT = "create_result"
    }
}