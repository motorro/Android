package com.motorro.cookbook.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.motorro.cookbook.appcore.viewbinding.BindingHost
import com.motorro.cookbook.appcore.viewbinding.WithViewBinding
import com.motorro.cookbook.appcore.viewbinding.bindView
import com.motorro.cookbook.appcore.viewbinding.withBinding
import com.motorro.cookbook.recipe.databinding.FragmentConfirmationBinding

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