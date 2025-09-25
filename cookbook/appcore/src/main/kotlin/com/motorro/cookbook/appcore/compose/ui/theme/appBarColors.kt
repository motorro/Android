package com.motorro.cookbook.appcore.compose.ui.theme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MaterialTheme.appBarColors() = TopAppBarDefaults.topAppBarColors(
    containerColor = colorScheme.primary,
    titleContentColor = colorScheme.onPrimary,
    navigationIconContentColor = colorScheme.onPrimary
)