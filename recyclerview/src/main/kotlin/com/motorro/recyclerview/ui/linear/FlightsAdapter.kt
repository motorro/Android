package com.motorro.recyclerview.ui.linear

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.motorro.recyclerview.databinding.VhFlightBinding
import com.motorro.recyclerview.ui.linear.data.FlightListItem
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char

class FlightsAdapter() : RecyclerView.Adapter<FlightsAdapter.FlightViewHolder>() {

    private var flights: List<FlightListItem> = emptyList()

    fun addFlights(flights: List<FlightListItem>) {
        this.flights += flights
        notifyItemRangeInserted(this.flights.size, flights.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        return FlightViewHolder(VhFlightBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return flights.size
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        holder.bind(flights[position] as FlightListItem.Flight)
    }

    class FlightViewHolder(private val binding: VhFlightBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(flight: FlightListItem.Flight) = with(binding) {
            flightNumber.text = flight.flightNumber
            time.text = timeFormatter.format(flight.dateTime)
            from.text = flight.from
            aircraft.text = flight.aircraft
        }

        companion object {
            val timeFormatter = LocalDateTime.Format {
                hour()
                char(':')
                minute()
            }
        }
    }
}