package com.motorro.recyclerview.ui.linear.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class FlightsDataSource(private val scope: CoroutineScope) {
    private data class Flights(
        val flights: List<FlightListItem> = emptyList(),
        val latestDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        val loading: Boolean = false
    )

    private val _flights = MutableStateFlow(Flights())

    init {
        loadMore()
    }

    val flights: StateFlow<List<FlightListItem>> = _flights.map { it.flights }.stateIn(
        scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun loadMore() {
        val soFar = _flights.value
        if (soFar.loading) return

        _flights.value = soFar.copy(
            flights = soFar.flights + FlightListItem.Loading,
            loading = true
        )

        scope.launch {
            Log.i(TAG, "Loading more flights from: ${soFar.latestDate}")
            val newFlights = loadFlights(soFar.latestDate)
            _flights.value = soFar.copy(
                flights = soFar.flights.drop(1) + listOf(FlightListItem.Date(soFar.latestDate)) + newFlights,
                latestDate = soFar.latestDate.plus(1, DateTimeUnit.DAY),
                loading = false
            )
        }
    }

    fun removeAt(position: Int) {
        val soFar = _flights.value
        if (soFar.loading) return

        Log.i(TAG, "Removing item at $position")
        _flights.value = soFar.copy(
            flights = soFar.flights.toMutableList().also { it.removeAt(position) }
        )
    }

    fun swap(from: Int, to: Int) {
        val soFar = _flights.value
        if (soFar.loading) return

        Log.i(TAG, "Swapping items $from and $to")
        _flights.value = soFar.copy(
            flights = soFar.flights.toMutableList().also {
                val fromItem = it[from]
                it[from] = it[to]
                it[to] = fromItem
            }
        )
    }

    companion object {
        private const val TAG = "FlightsDataSource"
    }
}