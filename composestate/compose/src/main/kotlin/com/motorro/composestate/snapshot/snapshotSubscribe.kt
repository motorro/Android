package com.motorro.composestate.snapshot

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.MutableSnapshot
import androidx.compose.runtime.snapshots.Snapshot

fun main() {
    val data: MutableState<String> = mutableStateOf("State 1")
    println("Initial state: ${data.value}")

    val readObserver: (Any) -> Unit = { readState ->
        if (readState == data) println("DATA WAS READ")
    }
    val writeObserver: (Any) -> Unit = { writtenState ->
        if (writtenState == data) println("DATA WAS WRITTEN")
    }

    // Subscribe to reads and writes
    val snapshot: MutableSnapshot = Snapshot.takeMutableSnapshot(readObserver, writeObserver)

    snapshot.enter {
        println("Before reading the data")
        println(data.value)
        println("Before writing the data")
        data.value = "State 2"
    }

    snapshot.dispose()
}