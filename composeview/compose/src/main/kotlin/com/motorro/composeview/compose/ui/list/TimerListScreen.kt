package com.motorro.composeview.compose.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.motorro.composeview.appcore.timer.model.TimerGesture
import com.motorro.composeview.appcore.timer.model.TimerViewState
import com.motorro.composeview.compose.R
import com.motorro.composeview.compose.ui.TimerView
import com.motorro.composeview.appcore.R as RC

@Composable
fun TimerListScreen(timers: List<TimerViewState>, onGesture: (Int, TimerGesture) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(RC.dimen.activity_horizontal_margin)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(RC.dimen.activity_vertical_margin))
    ) {
        // Fixed item (header)
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                text = stringResource(R.string.title_list)
            )
        }

        // List of timers
        items(timers, key = { it.id }) { timer ->
            TimerView(
                state = timer,
                modifier = Modifier.fillMaxWidth(),
                onGesture = onGesture
            )
        }
    }
}