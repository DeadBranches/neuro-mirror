package com.omnivoiceai.neuromirror.utils

import androidx.compose.ui.graphics.Color
import com.omnivoiceai.neuromirror.data.database.badge.Badge
import com.omnivoiceai.neuromirror.ui.events.UIEvent
import com.omnivoiceai.neuromirror.ui.events.UiNotificationData
import com.omnivoiceai.neuromirror.ui.events.UiNotificationType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

fun UIEvent.toUiNotification(): UiNotificationData? {
    return when (this) {
        is UIEvent.ShowNotification -> {
            val color = when (type) {
                UiNotificationType.Success -> Color(0xFF4CAF50)
                UiNotificationType.Error -> Color(0xFFF44336)
                UiNotificationType.Info -> Color.Gray
                UiNotificationType.Notification -> Color(0xFF2196F3)
            }
            UiNotificationData(message, icon, color)
        }

        is UIEvent.LoginSuccess -> UiNotificationData("Login effettuato!", "✅", Color(0xFF4CAF50))
        is UIEvent.Logout -> UiNotificationData("Logout effettuato", "🚪", Color.Gray)

        is UIEvent.BadgeUnlocked -> UiNotificationData(
            message = "Hai sbloccato un badge: ${badge.badgeKey}",
            emoji = emoji ?: "🎖️",
            color = Color(0xFF4CAF50)
        )
        else -> null
    }
}



object UiEventBus {
    private val _events = MutableSharedFlow<UIEvent>(extraBufferCapacity = 64)
    val events: SharedFlow<UIEvent> = _events

    fun notify(event: UIEvent) = _events.tryEmit(event)

    fun showNotification(
        message: String,
        type: UiNotificationType = UiNotificationType.Info,
        icon: String? = null
    ) {
        notify(UIEvent.ShowNotification(message, type, icon))
    }

    fun showBadge(badge: Badge, emotionEmojy: String? = null) {
        notify(UIEvent.BadgeUnlocked(badge, emotionEmojy))
    }


    fun showLoginSuccess() {
        showNotification("Login effettuato!", UiNotificationType.Success)
    }

    fun showError(message: String) {
        showNotification(message, UiNotificationType.Error, "")
    }
}
