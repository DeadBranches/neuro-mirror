package com.omnivoiceai.neuromirror.notifications.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.omnivoiceai.neuromirror.notifications.NeuroMirrorNotificationManager

class DailyReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val notificationManager = NeuroMirrorNotificationManager(applicationContext)
        notificationManager.showDailyReminderNotification()
        return Result.success()
    }
} 