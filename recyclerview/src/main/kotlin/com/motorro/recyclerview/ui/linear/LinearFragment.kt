package com.motorro.recyclerview.ui.linear

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.motorro.recyclerview.databinding.FragmentLinearBinding

class LinearFragment : Fragment() {

    private var _binding: FragmentLinearBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLinearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textHome.text = "This is linear Fragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}