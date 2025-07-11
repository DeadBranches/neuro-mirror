package com.omnivoiceai.neuromirror.utils

import com.omnivoiceai.neuromirror.data.database.badge.Badge
import com.omnivoiceai.neuromirror.domain.model.LoginEventType
import com.omnivoiceai.neuromirror.ui.events.UIEvent
import com.omnivoiceai.neuromirror.ui.events.UiNotificationData
import com.omnivoiceai.neuromirror.ui.events.UiNotificationType
import com.omnivoiceai.neuromirror.ui.theme.Purple40
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

fun UIEvent.toUiNotification(): UiNotificationData? {
    return when (this) {
        is UIEvent.ShowNotification -> {
            val color = type.color
            UiNotificationData(message, icon, color)
        }

        is UIEvent.BadgeUnlocked -> UiNotificationData(
            message = badge,
            emoji = emoji ?: "🎖️",
            color = Purple40
        )

        else -> null
    }
}

object UiEventBus {
    private val _events = MutableSharedFlow<UIEvent>(extraBufferCapacity = 64)
    val events: SharedFlow<UIEvent> = _events

    fun notify(event: UIEvent) = _events.tryEmit(event)

    fun showNotification(
        message: Any,
        type: UiNotificationType = UiNotificationType.Info,
        icon: String? = null
    ) {
        notify(UIEvent.ShowNotification(message, type, icon))
    }

    fun showBadge(badge: Badge, emotionEmojy: String? = null) {
        notify(UIEvent.BadgeUnlocked(badge, emotionEmojy))
    }

    fun showLoginSuccess() {
        showNotification(LoginEventType.Success, UiNotificationType.Success, "✅")
    }

    fun showLogout() {
        showNotification(LoginEventType.Logout, UiNotificationType.Info, "🚪")
    }

    fun showError(message: String) {
        showNotification(message, UiNotificationType.Error, "❌")
    }
}
