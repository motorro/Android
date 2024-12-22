package com.motorro.recyclerview.ui.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.motorro.recyclerview.databinding.FragmentGridBinding

class GridFragment : Fragment() {

    private var _binding: FragmentGridBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textGrid.text = "This is grid Fragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}