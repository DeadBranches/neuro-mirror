package com.omnivoiceai.neuromirror.notifications

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver(
    private val notificationScheduler: NotificationScheduler
) : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        notificationScheduler.scheduleMissingUserNotification()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        notificationScheduler.cancelMissingUserNotification()
    }
} 