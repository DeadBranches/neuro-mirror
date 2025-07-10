package com.omnivoiceai.neuromirror.ui.events

import androidx.compose.ui.graphics.Color
import com.omnivoiceai.neuromirror.data.database.badge.Badge

enum class UiNotificationType {
    Success, Error, Info, Notification
}

data class UiNotificationData(
    val message: Any,
    val emoji: String?,
    val color: Color
)


sealed class UIEvent {
    data class ShowNotification(
        val message: String,
        val type: UiNotificationType = UiNotificationType.Info,
        val icon: String? = null
    ) : UIEvent()

    object LoginSuccess : UIEvent()
    object Logout : UIEvent()

    data class BadgeUnlocked(val badge: Badge, val emoji: String? = null) : UIEvent()
}
