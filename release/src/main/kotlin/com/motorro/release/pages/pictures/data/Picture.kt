package com.motorro.release.pages.pictures.data

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class Picture(
    val vector: ImageVector,
    @field:StringRes val title: Int
)