package com.motorro.composestate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

// Non-savable class
private class Counters(count1: Int = 0, count2: Int = 0) {
    var count1: Int by mutableIntStateOf(count1)
    var count2: Int by mutableIntStateOf(count2)
}

@Composable
fun Counter() {

    val counterSaver = run {
        val count1Key = "count1"
        val count2Key = "count2"
        mapSaver(
            save = { mapOf(count1Key to it.count1, count2Key to it.count2) },
            restore = { Counters(it[count1Key] as Int, it[count2Key] as Int) }
        )
    }

    val count: Counters = rememberSaveable(saver = counterSaver) { Counters() }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
        Counter(count = count.count1, onChange = { count.count1 = it })
        Counter(count = count.count2, onChange = { count.count2 = it })
    }
}

@Composable
fun Counter(count: Int, onChange: (Int) -> Unit) {
    CounterView(
        count = count,
        onIncrement = { onChange(count + 1) },
        onDecrement = { onChange((count - 1).coerceAtLeast(0)) }
    )
}

@Preview
@Composable
fun CounterPreview() {
    Counter()
}