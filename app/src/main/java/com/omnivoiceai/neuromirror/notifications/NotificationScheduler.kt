package com.omnivoiceai.neuromirror.notifications

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.omnivoiceai.neuromirror.notifications.workers.DailyReminderWorker
import com.omnivoiceai.neuromirror.notifications.workers.MissingUserWorker
import com.omnivoiceai.neuromirror.notifications.workers.TestNotificationWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    companion object {
        const val DAILY_REMINDER_WORK_NAME = "daily_reminder_work"
        const val MISSING_USER_WORK_NAME = "missing_user_work"
        const val TEST_NOTIFICATION_WORK_NAME = "test_notification_work"

        private const val DAILY_REMINDER_HOUR = 19  // 7 PM
        private const val DAILY_REMINDER_MINUTE = 30
        private const val MISSING_USER_DELAY_MINUTES = 30L
        private const val TEST_NOTIFICATION_DELAY_SECONDS = 60L
    }

    private val workManager = WorkManager.getInstance(context)

    private val defaultConstraints = Constraints.Builder()
        .setRequiresBatteryNotLow(false)
        .build()

    fun scheduleDailyReminder() {
        val now = System.currentTimeMillis()
        val targetTime = Calendar.getInstance().apply {
            timeInMillis = now
            set(Calendar.HOUR_OF_DAY, DAILY_REMINDER_HOUR)
            set(Calendar.MINUTE, DAILY_REMINDER_MINUTE)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= now) add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = targetTime.timeInMillis - now

        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(defaultConstraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            DAILY_REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    fun scheduleMissingUserNotification() {
        val request = OneTimeWorkRequestBuilder<MissingUserWorker>()
            .setInitialDelay(MISSING_USER_DELAY_MINUTES, TimeUnit.MINUTES)
            .setConstraints(defaultConstraints)
            .build()

        workManager.enqueueUniqueWork(
            MISSING_USER_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun scheduleTestNotification() {
        val request = OneTimeWorkRequestBuilder<TestNotificationWorker>()
            .setInitialDelay(TEST_NOTIFICATION_DELAY_SECONDS, TimeUnit.SECONDS)
            .build()

        workManager.enqueue(request)
    }

    fun cancelMissingUserNotification() {
        workManager.cancelUniqueWork(MISSING_USER_WORK_NAME)
    }

    fun cancelAllNotifications() {
        workManager.cancelUniqueWork(DAILY_REMINDER_WORK_NAME)
        workManager.cancelUniqueWork(MISSING_USER_WORK_NAME)
    }
}