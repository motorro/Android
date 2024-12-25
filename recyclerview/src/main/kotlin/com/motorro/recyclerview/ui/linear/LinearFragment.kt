package com.motorro.recyclerview.ui.linear

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.motorro.recyclerview.databinding.FragmentLinearBinding
import com.motorro.recyclerview.ui.linear.data.loadFlights
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class LinearFragment : Fragment() {

    private var _binding: FragmentLinearBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: FlightsAdapter

    private var latestDate: LocalDate? = null

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
        binding.recyclerFlights.adapter = adapter
        binding.recyclerFlights.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        ItemTouchHelper(RemoveTouchHelper(adapter)).attachToRecyclerView(binding.recyclerFlights)
        ItemTouchHelper(SwapTouchHelper(adapter)).attachToRecyclerView(binding.recyclerFlights)
        binding.recyclerFlights.addOnScrollListener(LoadMoreScrollListener(
            layoutManager = binding.recyclerFlights.layoutManager as LinearLayoutManager,
            onLoadMore = ::loadFlights
        ))
        loadFlights()
    }

    private fun loadFlights() {
        viewLifecycleOwner.lifecycleScope.launch {
            val from = getFlightDate()
            Log.i(TAG, "Loading flights from: $from")
            adapter.addFlights(loadFlights(from))
        }
    }

    private fun getFlightDate(): LocalDate {
        val from = latestDate?.plus(1, DateTimeUnit.DAY) ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        latestDate = from
        return from
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LinearFragment"
    }
}