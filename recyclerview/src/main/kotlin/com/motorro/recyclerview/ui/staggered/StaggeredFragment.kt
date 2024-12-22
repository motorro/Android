package com.motorro.recyclerview.ui.staggered

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.motorro.recyclerview.databinding.FragmentStaggeredBinding

class StaggeredFragment : Fragment() {

    private var _binding: FragmentStaggeredBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaggeredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textNotifications.text = "This is staggered Fragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}