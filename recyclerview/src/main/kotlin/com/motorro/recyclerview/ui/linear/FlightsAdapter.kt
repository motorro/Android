package com.motorro.recyclerview.ui.linear

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.motorro.recyclerview.R
import com.motorro.recyclerview.databinding.VhDateHeaderBinding
import com.motorro.recyclerview.databinding.VhFlightBinding
import com.motorro.recyclerview.ui.linear.data.FlightListItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char

class FlightsAdapter() : RecyclerView.Adapter<FlightsAdapter.FlightViewHolder>() {

    private var flights: List<FlightListItem> = emptyList()

    fun addFlights(flights: List<FlightListItem>) {
        this.flights += flights
        notifyItemRangeInserted(this.flights.size, flights.size)
    }

    fun removeAt(position: Int) {
        this.flights -= flights[position]
        notifyItemRemoved(position)
    }

    fun swap(from: Int, to: Int) {
        val fromItem = flights[from]
        val toItem = flights[to]
        this.flights = this.flights.toMutableList().also {
            it[from] = toItem
            it[to] = fromItem
        }
        notifyItemMoved(from, to)
    }

    override fun getItemViewType(position: Int): Int = when(flights[position]) {
        is FlightListItem.Date -> R.layout.vh_date_header
        is FlightListItem.Flight -> R.layout.vh_flight
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder = when(viewType) {
        R.layout.vh_date_header -> FlightViewHolder.DateHeader(
            VhDateHeaderBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        R.layout.vh_flight -> FlightViewHolder.Flight(
            VhFlightBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else -> {
            throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun getItemCount(): Int {
        return flights.size
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        when (holder) {
            is FlightViewHolder.DateHeader -> holder.bind(flights[position] as FlightListItem.Date)
            is FlightViewHolder.Flight -> holder.bind(flights[position] as FlightListItem.Flight)
        }
    }

    sealed class FlightViewHolder(view: View): RecyclerView.ViewHolder(view) {
        class DateHeader(private val binding: VhDateHeaderBinding) : FlightViewHolder(binding.root) {
            fun bind(date: FlightListItem.Date) = with(binding) {
                root.text = dateFormatter.format(date.date)
            }

            companion object {
                private val dateFormatter = LocalDate.Format {
                    year()
                    char('-')
                    monthNumber()
                    char('-')
                    day()
                }
            }
        }

        class Flight(private val binding: VhFlightBinding) : FlightViewHolder(binding.root) {
            fun bind(flight: FlightListItem.Flight) = with(binding) {
                flightNumber.text = flight.flightNumber
                time.text = timeFormatter.format(flight.dateTime)
                from.text = flight.from
                aircraft.text = flight.aircraft
            }

            companion object {
                private val timeFormatter = LocalDateTime.Format {
                    hour()
                    char(':')
                    minute()
                }
            }
        }
    }
}