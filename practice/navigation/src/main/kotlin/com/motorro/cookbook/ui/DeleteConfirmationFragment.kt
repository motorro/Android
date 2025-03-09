package com.motorro.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.motorro.cookbook.R
import com.motorro.cookbook.databinding.FragmentConfirmationBinding

class DeleteConfirmationFragment : DialogFragment() {
    private val binding = FragmentBindingDelegate<FragmentConfirmationBinding>(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentConfirmationBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.withBinding {
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