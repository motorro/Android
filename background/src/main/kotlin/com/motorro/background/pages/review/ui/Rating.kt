package com.motorro.background.pages.review.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.background.R
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme

/**
 * A rating widget with 5 stars
 * @param rating Current rating 1..5
 * @param maxRating Maximum rating
 * @param onChange Is called when user changes rating
 * @param modifier Modifier
 */
@Composable
fun Rating(
    rating: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxRating: Int = 5
) {
    Card(modifier = modifier) {
        Row(modifier = Modifier.padding(AppDimens.margin_all_small)) {
            for (i in 1..maxRating) {
                IconButton(onClick = { onChange(i) }) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                        contentDescription = stringResource(R.string.desc_star_rating, i)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RatingPreview() {
    var rating by remember { mutableIntStateOf(3) }
    AppTheme {
        Rating(rating = rating, maxRating = 5, onChange = { rating = it })
    }
}
