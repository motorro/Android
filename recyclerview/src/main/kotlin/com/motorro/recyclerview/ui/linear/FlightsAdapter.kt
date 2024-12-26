package com.motorro.recyclerview.ui.linear

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motorro.recyclerview.R
import com.motorro.recyclerview.databinding.VhDateHeaderBinding
import com.motorro.recyclerview.databinding.VhFlightBinding
import com.motorro.recyclerview.databinding.VhLoadingBinding
import com.motorro.recyclerview.ui.linear.data.FlightListItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char

class FlightsAdapter(private val onClick: (FlightListItem.Flight) -> Unit) : ListAdapter<FlightListItem, FlightsAdapter.FlightViewHolder>(FlightItemDiff) {

    override fun getItemViewType(position: Int): Int = when(getItem(position)) {
        is FlightListItem.Date -> R.layout.vh_date_header
        is FlightListItem.Flight -> R.layout.vh_flight
        FlightListItem.Loading -> R.layout.vh_loading
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
            ),
            onClick
        )
        R.layout.vh_loading -> FlightViewHolder.Loading(
            VhLoadingBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else -> {
            throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        when (holder) {
            is FlightViewHolder.DateHeader -> holder.bind(getItem(position) as FlightListItem.Date)
            is FlightViewHolder.Flight -> holder.bind(getItem(position) as FlightListItem.Flight)
            is FlightViewHolder.Loading -> Unit
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

        class Flight(private val binding: VhFlightBinding, private val onClick: (FlightListItem.Flight) -> Unit) : FlightViewHolder(binding.root) {

            private lateinit var boundFlight: FlightListItem.Flight

            init {
                binding.root.setOnClickListener {
                    onClick(boundFlight)
                }
            }

            fun bind(flight: FlightListItem.Flight) = with(binding) {
                boundFlight = flight
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

        class Loading(private val binding: VhLoadingBinding) : FlightViewHolder(binding.root)
    }
}

private object FlightItemDiff : DiffUtil.ItemCallback<FlightListItem>() {
    override fun areItemsTheSame(oldItem: FlightListItem, newItem: FlightListItem): Boolean = when {
        oldItem is FlightListItem.Date && newItem is FlightListItem.Date -> oldItem.date == newItem.date
        oldItem is FlightListItem.Flight && newItem is FlightListItem.Flight -> oldItem.id == newItem.id
        oldItem is FlightListItem.Loading && newItem is FlightListItem.Loading -> true
        else -> false
    }

    override fun areContentsTheSame(oldItem: FlightListItem, newItem: FlightListItem): Boolean = oldItem == newItem
}