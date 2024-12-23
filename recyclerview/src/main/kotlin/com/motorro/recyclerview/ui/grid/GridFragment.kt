package com.motorro.recyclerview.ui.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.motorro.recyclerview.databinding.FragmentGridBinding
import com.motorro.recyclerview.ui.grid.data.loadPictures

class GridFragment : Fragment() {

    private var _binding: FragmentGridBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PicturesAdapter

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
        adapter = PicturesAdapter()
        binding.recyclerPics.adapter = adapter
        adapter.setPictures(loadPictures())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}