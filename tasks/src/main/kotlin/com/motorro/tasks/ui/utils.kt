package com.motorro.tasks.ui

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import com.motorro.tasks.R
import com.motorro.tasks.app.data.AppError
import com.motorro.tasks.app.data.getMessage

/**
 * Makes a snackbar for the application error
 */
fun AppError.errorSnackbar(context: Context): SnackbarVisuals = object : SnackbarVisuals {
    override val actionLabel: String = if (retriable) {
        context.getString(R.string.btn_retry)
    } else {
        context.getString(R.string.btn_close)
    }
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite
    override val message: String = getMessage(context)
    override val withDismissAction: Boolean = retriable
}