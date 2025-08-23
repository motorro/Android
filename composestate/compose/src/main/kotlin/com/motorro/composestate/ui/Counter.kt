package com.motorro.composestate.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.composestate.R
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

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
    val inRange1 by remember {
        derivedStateOf { count.count1 in 2..6 }
    }

    LaunchedEffect(count.count1) {
        Log.i("Counter", "Resetting count 2")
        count.count2 = 0
        while (currentCoroutineContext().isActive) {
            kotlinx.coroutines.delay(500)
            count.count2++
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Counter(count = count.count1, onChange = { count.count1 = it })
            Text(text = stringResource(R.string.lbl_in_range, inRange1))
        }
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Counter(count = count.count2, onChange = { count.count2 = it })
            InRange(value = { count.count2 in 2..6 })
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { count.count1 }.collect {
            Log.i("Counter", "Count 1: $it")
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { inRange1 }.collect {
            Log.i("Counter", "In range: $it")
        }
    }
}

@Composable
fun Counter(count: Int, modifier: Modifier = Modifier, onChange: (Int) -> Unit) {
    CounterView(
        count = count,
        modifier = modifier,
        onIncrement = { onChange(count + 1) },
        onDecrement = { onChange((count - 1).coerceAtLeast(0)) }
    )
}

@Preview
@Composable
fun CounterPreview() {
    Counter()
}