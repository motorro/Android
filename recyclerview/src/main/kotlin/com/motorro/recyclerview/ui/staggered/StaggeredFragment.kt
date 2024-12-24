package com.motorro.recyclerview.ui.staggered

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.motorro.recyclerview.R
import com.motorro.recyclerview.databinding.FragmentStaggeredBinding
import com.motorro.recyclerview.ui.staggered.data.loadLetters

class StaggeredFragment : Fragment() {

    private var _binding: FragmentStaggeredBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LettersAdapter

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
        adapter = LettersAdapter()
        binding.recyclerPics.adapter = adapter
        binding.recyclerPics.addItemDecoration(
            StaggeredDecoration(
                spanCount = resources.getInteger(R.integer.grid_span),
                spacing = resources.getDimensionPixelSize(R.dimen.content_margin)
            )
        )
        adapter.setLetters(loadLetters())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}