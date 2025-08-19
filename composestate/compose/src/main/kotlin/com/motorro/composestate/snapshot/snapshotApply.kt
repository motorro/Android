package com.motorro.composestate.snapshot

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.MutableSnapshot
import androidx.compose.runtime.snapshots.Snapshot

fun main() {
    val data: MutableState<String> = mutableStateOf("")
    println("Initial state: ${data.value}")

    data.value = "State 1"
    println("Modification 1: ${data.value}")

    // Take MUTABLE snapshot
    val snapshot: MutableSnapshot = Snapshot.takeMutableSnapshot()

    snapshot.enter {
        data.value = "State 2"
        println("Meanwhile in Snapshot: ${data.value}")
    }

    println("Before apply: ${data.value}")
    // Apply changes to the state
    snapshot.apply()
    println("After apply: ${data.value}")

    snapshot.dispose()
}