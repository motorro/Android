package com.motorro.recyclerview.ui.linear

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.motorro.recyclerview.databinding.FragmentLinearBinding
import com.motorro.recyclerview.ui.linear.data.loadFlights
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LinearFragment : Fragment() {

    private var _binding: FragmentLinearBinding? = null

    private val binding get() = _binding!!

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
        binding.recyclerFlights.adapter = adapter
        loadFlights()
    }

    private fun loadFlights() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.addFlights(loadFlights(getFlightDate()))
        }
    }

    private fun getFlightDate(): LocalDate {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}