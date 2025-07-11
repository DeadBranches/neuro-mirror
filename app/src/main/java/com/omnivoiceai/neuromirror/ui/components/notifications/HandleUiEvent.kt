package com.omnivoiceai.neuromirror.ui.components.notifications

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.badge.Badge
import com.omnivoiceai.neuromirror.data.database.badge.BadgeCategory
import com.omnivoiceai.neuromirror.data.database.note.EmotionDetected
import com.omnivoiceai.neuromirror.domain.model.LoginEventType
import com.omnivoiceai.neuromirror.ui.events.UiNotificationData
import com.omnivoiceai.neuromirror.utils.UiEventBus
import com.omnivoiceai.neuromirror.utils.toUiNotification

fun getBadgeLabel(context: Context, badge: Badge): String {
    val parts = badge.badgeKey.split("_")
    if (parts.size < 2) return badge.badgeKey

    val category = badge.category
    val subType = parts.getOrNull(1)?.lowercase()?.replaceFirstChar { it.uppercaseChar() } ?: ""
    val count = parts.lastOrNull()?.toIntOrNull()

    val suffix = when (count) {
        1 -> context.getString(R.string.badge_suffix_1)
        null -> ""
        else -> "$count ${context.getString(R.string.badge_suffix_n)}"
    }

    return when (category) {
        BadgeCategory.NOTE -> "${context.getString(R.string.badge_prefix_note)} $suffix"
        BadgeCategory.QUESTION -> "${context.getString(R.string.badge_prefix_question)} $suffix"
        BadgeCategory.MESSAGE -> "${context.getString(R.string.badge_prefix_message)} $suffix"
        BadgeCategory.EMOTION -> {
            val emotion = EmotionDetected.entries.firstOrNull {
                it.name.equals(subType, ignoreCase = true)
            }
            val emotionStr = emotion?.getLabelRes()?.let { context.getString(it) } ?: subType
            "${context.getString(R.string.badge_prefix_emotion)} $emotionStr $suffix"
        }
    }.trim()
}

private fun resolveMessage(context: Context, data: UiNotificationData): String {
    return when (val msg = data.message) {
        is String -> msg
        is Badge -> context.getString(
            R.string.badge_unlocked_message,
            getBadgeLabel(context, msg)
        )
        is LoginEventType -> when (msg) {
            LoginEventType.Success -> context.getString(R.string.login_success)
            LoginEventType.Logout -> context.getString(R.string.logout_success)
        }
        else -> msg.toString()
    }
}

@Composable
fun HandleUiEvents(snackbarHostState: SnackbarHostState, snackbarColor: MutableState<Color>) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        UiEventBus.events.collect { event ->
            val data = event.toUiNotification()
            if (data != null) {
                snackbarColor.value = data.color
                val prefix = data.emoji?.let { "$it " } ?: ""
                val message = resolveMessage(context, data)
                snackbarHostState.showSnackbar(prefix + message)
            }
        }
    }
}
