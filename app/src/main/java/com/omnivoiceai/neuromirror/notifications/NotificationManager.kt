package com.omnivoiceai.neuromirror.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.omnivoiceai.neuromirror.MainActivity
import com.omnivoiceai.neuromirror.R

class NeuroMirrorNotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "neuromirror_notifications"
        const val NOTIFICATION_ID_MISSING = 1001
        const val NOTIFICATION_ID_DAILY = 1002
        const val NOTIFICATION_ID_TEST = 1003
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                enableVibration(true)
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Permission not required for API < 33
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    fun showMissingUserNotification() {
        if (!hasNotificationPermission()) return

        val title = context.getString(R.string.notification_missing_title)
        val content = context.getString(R.string.notification_missing_body)
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(createPendingIntent())
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID_MISSING, notification)
        }
    }

    @SuppressLint("MissingPermission")
    fun showDailyReminderNotification() {
        if (!hasNotificationPermission()) return

        val title = context.getString(R.string.notification_daily_title)
        val content = context.getString(R.string.notification_daily_body)
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(createPendingIntent())
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID_DAILY, notification)
        }
    }

    @SuppressLint("MissingPermission")
    fun showTestNotification() {
        if (!hasNotificationPermission()) return

        val title = context.getString(R.string.notification_test_title)
        val content = context.getString(R.string.notification_test_body)
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(createPendingIntent())
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID_TEST, notification)
        }
    }
} 