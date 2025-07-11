package com.omnivoiceai.neuromirror.ui.screens.settings.components

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.omnivoiceai.neuromirror.R

@Composable
fun NotificationPreferences() {
    val context = LocalContext.current

    SettingLineItem(
        title = stringResource(R.string.manage_notifications),
        onClick = {
        val intent =
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }

        context.startActivity(intent)
    })
}
