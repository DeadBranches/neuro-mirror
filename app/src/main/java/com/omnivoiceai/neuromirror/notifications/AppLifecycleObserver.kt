package com.omnivoiceai.neuromirror.notifications

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver(
    private val notificationScheduler: NotificationScheduler
) : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // App went to background, schedule missing user notification
        notificationScheduler.scheduleMissingUserNotification()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // App came to foreground, cancel missing user notification
        notificationScheduler.cancelMissingUserNotification()
    }
} 