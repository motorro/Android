package com.motorro.tasks.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.motorro.tasks.R
import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState

/**
 * Task-list
 */
@Composable
fun TaskListScreen(state: AppUiState.TaskList, onGesture: (AppGesture) -> Unit) {
    ScreenScaffold(
        title = stringResource(R.string.title_login),
        onBack = { onGesture(AppGesture.Back) },
        actions = {
            IconButton(onClick = { onGesture(AppGesture.Logout) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_logout),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = "btn_logout"
                )
            }
        },
        content = { padding ->
            Text(modifier = Modifier.padding(padding), text = "TODO: Tasks")
        }
    )
}