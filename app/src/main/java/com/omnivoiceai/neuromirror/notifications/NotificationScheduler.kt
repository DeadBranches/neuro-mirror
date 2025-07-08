package com.omnivoiceai.neuromirror.notifications

import android.content.Context
import androidx.work.*
import com.omnivoiceai.neuromirror.notifications.workers.DailyReminderWorker
import com.omnivoiceai.neuromirror.notifications.workers.MissingUserWorker
import com.omnivoiceai.neuromirror.notifications.workers.TestNotificationWorker
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    companion object {
        const val DAILY_REMINDER_WORK_NAME = "daily_reminder_work"
        const val MISSING_USER_WORK_NAME = "missing_user_work"
        const val TEST_NOTIFICATION_WORK_NAME = "test_notification_work"
        
        private const val DAILY_REMINDER_HOUR = 19 // 7 PM
        private const val DAILY_REMINDER_MINUTE = 30 // 30 minutes
        private const val MISSING_USER_DELAY_MINUTES = 30L
        private const val TEST_NOTIFICATION_DELAY_SECONDS = 60L
    }

    private val workManager = WorkManager.getInstance(context)

    fun scheduleDailyReminder() {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, DAILY_REMINDER_HOUR)
            set(Calendar.MINUTE, DAILY_REMINDER_MINUTE)
            set(Calendar.SECOND, 0)
        }

        // If the time has passed for today, schedule for tomorrow
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val delay = calendar.timeInMillis - System.currentTimeMillis()

        val dailyReminderRequest = OneTimeWorkRequestBuilder<DailyReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(
            DAILY_REMINDER_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            dailyReminderRequest
        )

        // Schedule recurring daily reminder
        val dailyRecurringRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            "${DAILY_REMINDER_WORK_NAME}_recurring",
            ExistingPeriodicWorkPolicy.REPLACE,
            dailyRecurringRequest
        )
    }

    fun scheduleMissingUserNotification() {
        val missingUserRequest = OneTimeWorkRequestBuilder<MissingUserWorker>()
            .setInitialDelay(MISSING_USER_DELAY_MINUTES, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(
            MISSING_USER_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            missingUserRequest
        )
    }

    fun scheduleTestNotification() {
        val testRequest = OneTimeWorkRequestBuilder<TestNotificationWorker>()
            .setInitialDelay(TEST_NOTIFICATION_DELAY_SECONDS, TimeUnit.SECONDS)
            .build()

        workManager.enqueue(testRequest)
    }

    fun cancelMissingUserNotification() {
        workManager.cancelUniqueWork(MISSING_USER_WORK_NAME)
    }

    fun cancelAllNotifications() {
        workManager.cancelUniqueWork(DAILY_REMINDER_WORK_NAME)
        workManager.cancelUniqueWork("${DAILY_REMINDER_WORK_NAME}_recurring")
        workManager.cancelUniqueWork(MISSING_USER_WORK_NAME)
    }
} 