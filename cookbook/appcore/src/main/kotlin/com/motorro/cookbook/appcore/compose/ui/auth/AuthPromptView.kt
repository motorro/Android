package com.motorro.cookbook.appcore.compose.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
@OptIn(ExperimentalMaterial3Api::class)
fun AuthPromptView(onLoginClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppDimens.margin_all),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_auth),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            contentDescription = stringResource(id = R.string.desc_authenticate),
            modifier = Modifier.size(dimensionResource(id = R.dimen.full_page_icon))
        )

        Text(
            text = stringResource(id = R.string.str_auth),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = AppDimens.spacer)
        )

        Button(
            onClick = onLoginClick,
            modifier = Modifier.padding(top = AppDimens.spacer)
        ) {
            Text(text = stringResource(id = R.string.btn_login))
        }
    }
}

// --- Previews ---
@Preview(showBackground = true)
@Composable
fun AuthPromptViewPreview() {
    CookbookTheme {
        AuthPromptView(onLoginClick = {})
    }
}
