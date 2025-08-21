package com.motorro.composestate.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.composestate.R

@Composable
fun Counter() {
    var count: Int by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { count = (count - 1).coerceAtLeast(0) }) {
            Text(text = stringResource(R.string.btn_decrement))
        }

        Text(
            text = stringResource(R.string.lbl_count, count),
            style = MaterialTheme.typography.headlineMedium
        )

        Button(onClick = { count++ }) {
            Text(text = stringResource(R.string.btn_increment))
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { count }.collect {
            Log.i("Counter", "Count: $it")
        }
    }
}

@Preview
@Composable
fun CounterPreview() {
    Counter()
}