package com.motorro.recyclerview.ui.linear

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.motorro.recyclerview.databinding.FragmentLinearBinding
import com.motorro.recyclerview.ui.linear.data.FlightsDataSource
import kotlinx.coroutines.launch

class LinearFragment : Fragment() {

    private var _binding: FragmentLinearBinding? = null

    private val binding get() = _binding!!

    private lateinit var dataSource: FlightsDataSource
    private lateinit var adapter: FlightsAdapter

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
        adapter = FlightsAdapter()
        dataSource = FlightsDataSource(viewLifecycleOwner.lifecycleScope)
        binding.recyclerFlights.adapter = adapter
        binding.recyclerFlights.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        ItemTouchHelper(RemoveTouchHelper(dataSource)).attachToRecyclerView(binding.recyclerFlights)
        ItemTouchHelper(SwapTouchHelper(dataSource)).attachToRecyclerView(binding.recyclerFlights)
        binding.recyclerFlights.addOnScrollListener(LoadMoreScrollListener(
            layoutManager = binding.recyclerFlights.layoutManager as LinearLayoutManager,
            onLoadMore = dataSource::loadMore
        ))

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dataSource.flights.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}