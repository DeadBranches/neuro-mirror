package com.omnivoiceai.neuromirror.ui.screens.settings.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.notifications.NotificationScheduler

@Composable
fun NotificationTest(){
    val context = LocalContext.current
    NotificationTestSettings(
        onTestNotification = {
            val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true // Permission not required for API < 33
            }

            if (hasPermission) {
                try {
                    val scheduler = NotificationScheduler(context)
                    scheduler.scheduleTestNotification()
                    Toast.makeText(
                        context,
                        context.getString(R.string.test_notification_scheduled),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: SecurityException) {
                    Toast.makeText(
                        context,
                        "Notification permission required",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    "Please grant notification permission in app settings",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )
}