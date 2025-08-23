package com.motorro.composestate.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.composestate.R

@Composable
fun InRange(modifier: Modifier = Modifier, value: () -> Boolean) {
    Box(
        modifier = modifier
            .width(96.dp)
            .height(24.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
            )
            .drawBehind {
                drawRect(
                    color = Color.Green,
                    size = size.copy(width = size.width / 2)
                )
                drawRect(
                    topLeft = Offset(x = size.width / 2, y = 0f),
                    color = Color.Red,
                    size = size.copy(width = size.width / 2)
                )
            }
    ) {

        val iconModifier = Modifier.offset(
            x = if (value()) 12.dp else 60.dp,
        )

        Icon(
            painterResource(R.drawable.ic_dot),
            stringResource(R.string.desc_in_range),
            modifier = iconModifier,
            tint = Color.Black
        )
    }
}

@Preview
@Composable
fun InRangePreview() {
    var inRange by remember { mutableStateOf(false) }
    InRange(
        modifier = Modifier.clickable {
            inRange = inRange.not()
        },
        value = { inRange }
    )
}