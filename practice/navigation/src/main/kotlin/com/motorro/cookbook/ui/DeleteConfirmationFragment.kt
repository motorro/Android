package com.motorro.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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
            text.text = TODO("Get title from arguments")
            ok.setOnClickListener {
                setResult(true)
            }
            cancel.setOnClickListener {
                setResult(false)
            }
        }
    }

    private fun setResult(result: Boolean) {
        TODO("Set result and close")
    }
}