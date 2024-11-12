package com.motorro.activitynav.data

const val MIN_LENGTH = 30

fun Task.print(): String {
    val maxLength = activities.maxOfOrNull { it.activityId.id.length + 20 } ?: MIN_LENGTH

    fun data(data: String): String = "| ${data.padEnd(maxLength)} |" + System.lineSeparator()
    fun header(): String = "|${"=".repeat(maxLength+2)}|" + System.lineSeparator()
    fun divider(): String = "| ${"-".repeat(maxLength)} |" + System.lineSeparator()

    val result = StringBuilder(header())
    result.append(data("Task $id:"))
    result.append(divider())
    activities.reversed().forEach {
        result.append(data("Activity: ${it.activityId.id}"))
        result.append(data("State: ${it.state.name}"))
        result.append(data("Intents: ${it.intentCount}"))
        result.append(divider())
    }
    return result.toString()
}