package com.motorro.cookbook.appcore.compose.ui.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(0.8f))
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingViewPreview() {
    CookbookTheme {
        LoadingView()
    }
}
