package com.motorro.cookbook.appcore.compose.ui.loading

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.appcore.compose.ui.theme.appBarColors

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoadingScreen(title: String, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(title) },
                colors = MaterialTheme.appBarColors()
            )
        }
    ) { paddingValues ->
        LoadingView(Modifier.padding(paddingValues))
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    CookbookTheme {
        LoadingScreen(title = "Loading")
    }
}