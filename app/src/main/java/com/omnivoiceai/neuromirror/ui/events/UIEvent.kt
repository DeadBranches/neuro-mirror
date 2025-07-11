package com.omnivoiceai.neuromirror.ui.events

import androidx.compose.ui.graphics.Color
import com.omnivoiceai.neuromirror.data.database.badge.Badge
import com.omnivoiceai.neuromirror.ui.theme.ErrorColor
import com.omnivoiceai.neuromirror.ui.theme.InfoColor
import com.omnivoiceai.neuromirror.ui.theme.NotificationColor
import com.omnivoiceai.neuromirror.ui.theme.SuccessColor

enum class UiNotificationType(val color: Color) {
    Success(SuccessColor),
    Error(ErrorColor),
    Info(InfoColor),
    Notification(NotificationColor)
}


data class UiNotificationData(
    val message: Any,
    val emoji: String?,
    val color: Color
)


sealed class UIEvent {
    data class ShowNotification(
        val message: Any,
        val type: UiNotificationType = UiNotificationType.Info,
        val icon: String? = null
    ) : UIEvent()

    data class BadgeUnlocked(val badge: Badge, val emoji: String? = null) : UIEvent()
}
