package com.motorro.notifications.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.motorro.composecore.ui.ScreenScaffold
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.notifications.R
import com.motorro.notifications.data.MainScreenGesture

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NotificationEnableScreen(onGesture: (MainScreenGesture) -> Unit) {
    ScreenScaffold(
        title = stringResource(R.string.app_name),
        onBack = { onGesture(MainScreenGesture.Back) },
        content = { paddingValues ->
            NotificationRequirementsNotMetContent(
                modifier = Modifier.padding(paddingValues),
                onGesture
            )
        }
    )
}

/**
 * Notification check screen
 */
@Composable
private fun NotificationRequirementsNotMetContent(modifier: Modifier = Modifier, onGesture: (MainScreenGesture) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
        onGesture(MainScreenGesture.NotificationPermissionRequested(granted))
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = object : LifecycleEventObserver {
            var wasStopped = false
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (Lifecycle.Event.ON_STOP == event) {
                    wasStopped = true
                    return
                }

                if (wasStopped && Lifecycle.Event.ON_RESUME == event) {
                    lifecycleOwner.lifecycle.removeObserver(this)
                    onGesture(MainScreenGesture.RecheckNotificationPermissions)
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = modifier.padding(AppDimens.margin_all).fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppDimens.vertical_margin_small)
            ) {
                Text(
                    text = stringResource(R.string.notification_explanation),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(AppDimens.margin_all)
                )
            }
        }
        Button(onClick = { openAppSettings(context) }) {
            Text(text = stringResource(R.string.notification_settings))
        }
    }
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}