package com.omnivoiceai.neuromirror

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.omnivoiceai.neuromirror.notifications.AppLifecycleObserver
import com.omnivoiceai.neuromirror.notifications.NotificationScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin

class NeuroMirrorApplication: Application(), KoinComponent {
    
    private lateinit var notificationScheduler: NotificationScheduler
    private lateinit var lifecycleObserver: AppLifecycleObserver
    
    override fun onCreate(){
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@NeuroMirrorApplication)
            workManagerFactory()
            modules (appModule )
        }

        // Initialize notification system
        notificationScheduler = NotificationScheduler(this)
        lifecycleObserver = AppLifecycleObserver(notificationScheduler)

        // Start observing app lifecycle
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)

        // Schedule daily reminder
        notificationScheduler.scheduleDailyReminder()
    }
}