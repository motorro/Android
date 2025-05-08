package com.motorro.cookbook.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.motorro.cookbook.app.R
import com.motorro.cookbook.app.databinding.FragmentConfirmationBinding
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding

class DeleteConfirmationFragment : DialogFragment(), WithViewBinding<FragmentConfirmationBinding> by BindingHost() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(
        container,
        FragmentConfirmationBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            text.text = getString(R.string.text_delete, DeleteConfirmationFragmentArgs.fromBundle(requireArguments()).title)
            ok.setOnClickListener {
                setResult(true)
            }
            cancel.setOnClickListener {
                setResult(false)
            }
        }
    }

    private fun setResult(result: Boolean) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            CONFIRMATION_RESULT,
            result
        )
        findNavController().navigateUp()
    }

    companion object {
        const val CONFIRMATION_RESULT = "confirmation_result"
    }
}