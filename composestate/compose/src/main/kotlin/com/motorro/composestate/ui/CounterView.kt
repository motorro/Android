package com.motorro.composestate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.motorro.composestate.R

@Composable
fun CounterView(count: Int, onIncrement: () -> Unit, onDecrement: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onDecrement) {
            Text(text = stringResource(R.string.btn_decrement))
        }

        Text(
            text = stringResource(R.string.lbl_count, count),
            style = MaterialTheme.typography.headlineMedium
        )

        Button(onClick = onIncrement) {
            Text(text = stringResource(R.string.btn_increment))
        }
    }
}