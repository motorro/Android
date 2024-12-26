package com.motorro.recyclerview.ui.linear.data

import com.github.javafaker.Faker
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime

/**
 * Flights data
 */
data class Flights(val flights: List<FlightListItem> = emptyList())

/**
 * Flight list item
 */
sealed class FlightListItem {
    /**
     * Date header
     */
    data class Date(val date: LocalDate): FlightListItem()

    /**
     * Flight
     */
    data class Flight(
        val id: Int,
        val dateTime: LocalDateTime,
        val flightNumber: String,
        val from: String,
        val aircraft: String
    ): FlightListItem()

    /**
     * Loading item
     */
    data object Loading: FlightListItem()
}

private var nextFlightId = 1
private const val DELAY = 1500L
private val faker = Faker()

suspend fun loadFlights(atDate: LocalDate): List<FlightListItem> {
    // Simulate network delay
    delay(DELAY)
    return (0..23).map {
        FlightListItem.Flight(
            id = nextFlightId++,
            dateTime = atDate.atTime(it, 0),
            flightNumber = faker.numerify(faker.letterify("??-####", true)),
            from = faker.address().city(),
            aircraft = faker.aviation().aircraft()
        )
    }
}
