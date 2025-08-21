package com.motorro.composestate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

// Non-savable class
private class Counters {
    var count1: Int by mutableIntStateOf(0)
    var count2: Int by mutableIntStateOf(0)
}

@Composable
fun Counter() {

    val count: Counters = remember { Counters() }

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