package com.motorro.cookbook.appcore.compose.ui.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.cookbook.appcore.R
import com.motorro.cookbook.appcore.compose.ui.theme.AppDimens
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme

@Composable
fun ErrorView(errorMessage: String, onAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_network_error),
            contentDescription = stringResource(id = R.string.desc_loading_error),
            modifier = Modifier.size(dimensionResource(id = R.dimen.full_page_icon)),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(AppDimens.spacer))

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(AppDimens.spacer))

        Button(onClick = onAction) {
            Text(text = stringResource(id = R.string.btn_refresh))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorViewPreview() {
    CookbookTheme {
        ErrorView(
            errorMessage = "Error loading data",
            onAction = {}
        )
    }
}