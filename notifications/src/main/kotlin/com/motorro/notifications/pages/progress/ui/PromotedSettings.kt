package com.motorro.notifications.pages.progress.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.notifications.R
import com.motorro.notifications.pages.progress.data.ProgressGesture

@Composable
@RequiresApi(Build.VERSION_CODES.BAKLAVA)
fun PromotedSettings(modifier: Modifier = Modifier, onGesture: (ProgressGesture) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

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
                    onGesture(ProgressGesture.RecheckPromo)
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
        verticalArrangement = Arrangement.spacedBy(AppDimens.vertical_margin),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = modifier.padding(AppDimens.margin_all).fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppDimens.vertical_margin_small)
            ) {
                Text(
                    text = stringResource(R.string.txt_enable_promoted),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(AppDimens.margin_all)
                )
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(fraction = 0.6f),
            onClick = { openAppSettings(context) { onGesture(ProgressGesture.SkipPromo) } }
        ) {
            Text(text = stringResource(R.string.btn_grant_promoted))
        }
        Button(
            modifier = Modifier.fillMaxWidth(fraction = 0.6f),
            onClick = { onGesture(ProgressGesture.SkipPromo) }) {
            Text(text = stringResource(R.string.btn_skip_promoted))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
private fun openAppSettings(context: Context, onFailure: () -> Unit) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_PROMOTION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    if (null != intent.resolveActivity(context.packageManager)) {
        context.startActivity(intent)
    } else {
        onFailure()
    }
}