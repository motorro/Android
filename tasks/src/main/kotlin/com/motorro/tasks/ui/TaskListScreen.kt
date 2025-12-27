package com.motorro.tasks.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.composecore.ui.ScreenScaffold
import com.motorro.tasks.R
import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState
import com.motorro.tasks.data.Task
import com.motorro.tasks.data.TaskId
import com.motorro.tasks.data.UserName
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Task-list
 */
@Composable
fun TaskListScreen(state: AppUiState.TaskList, onGesture: (AppGesture) -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    if (null != state.error) {
        LaunchedEffect(state.error) {
            if (SnackbarResult.ActionPerformed == snackbarHostState.showSnackbar(state.error.errorSnackbar(context))) {
                onGesture(AppGesture.DismissError)
            }
        }
    }

    val angle = remember(state.isLoading) {
        Animatable(0f)
    }
    LaunchedEffect(state.isLoading) {
        launch {
            if (state.isLoading) {
                angle.animateTo(
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        tween(2000, easing = LinearEasing)
                    )
                )
            }
        }
    }

    ScreenScaffold(
        title = stringResource(R.string.title_task_list),
        onBack = { onGesture(AppGesture.Back) },
        actions = {
            IconButton(onClick = { onGesture(AppGesture.Refresh) }, enabled = state.isLoading.not()) {
                Icon(
                    modifier = Modifier.rotate(angle.value),
                    painter = painterResource(R.drawable.ic_refresh),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = stringResource(R.string.btn_refresh)
                )
            }
            IconButton(onClick = { onGesture(AppGesture.Logout) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_logout),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = stringResource(R.string.btn_logout)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    action = {
                        Text(
                            text = data.visuals.actionLabel ?: stringResource(R.string.btn_close),
                            color = MaterialTheme.colorScheme.inversePrimary,
                            modifier = Modifier.padding(4.dp).clickable { data.performAction() }
                        )
                    },
                    dismissAction = {
                        if (data.visuals.withDismissAction) {
                            Text(
                                text = stringResource(R.string.btn_dismiss),
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                modifier = Modifier.padding(4.dp).clickable { data.dismiss() }
                            )
                        }
                    },
                    content = {
                        Text(data. visuals. message)
                    }
                )
            }
        },
        fab = {
            FloatingActionButton(onClick = { onGesture(AppGesture.TaskList.AddClicked) }) {
                Icon(Icons.Filled.Add, stringResource(R.string.btn_add))
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (state.tasks.isEmpty()) {
                    Icon(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        painter = painterResource(R.drawable.ic_add),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2F),
                        contentDescription = stringResource(R.string.hint_add)
                    )
                }
                LazyColumn(Modifier.fillMaxSize()) {
                    items(state.tasks.toList(), key = { it.id.id }) {
                        TaskItem(it, onGesture)
                    }
                }
            }
        }
    )
}

private val DUE_FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

@Composable
fun TaskItem(data: Task, onGesture: (AppGesture) -> Unit) {
    val due = data.due?.toJavaLocalDateTime()?.let(DUE_FORMAT::format)
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onGesture(AppGesture.TaskList.TaskClicked(data.id)) },
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = data.complete, onCheckedChange = { onGesture(AppGesture.TaskList.TaskToggled(data.id)) })
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = data.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (null != due) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = due,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
fun TaskItemPreview() {
    val task = Task(
        id = TaskId(id = "task1"),
        author = UserName("User"),
        title = "Task 1",
        description = "The first task",
        complete = false,
        due = LocalDateTime(
            year = 2024,
            month = 10,
            day = 22,
            hour = 6,
            minute = 36,
            second = 0, nanosecond = 0
        )
    )
    TaskItem(
        data = task,
        onGesture = {}
    )
}