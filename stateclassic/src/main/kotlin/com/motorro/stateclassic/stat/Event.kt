package com.motorro.stateclassic.stat

/**
 * Event
 */
interface Event {
    val name: String
    val properties: Map<String, String>
}