package com.omnivoiceai.neuromirror.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.badge.Badge
import com.omnivoiceai.neuromirror.data.database.badge.BadgeCategory
import com.omnivoiceai.neuromirror.data.database.note.EmotionDetected
import com.omnivoiceai.neuromirror.data.database.note.toEmoji
import com.omnivoiceai.neuromirror.utils.getBadgeColorForLevel

@Composable
fun UserBadges(
    badges: List<Badge>,
    onSeeOthersClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val visibleBadges = badges.take(8)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.badges),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (visibleBadges.isEmpty()) {
            Text(
                stringResource(R.string.badges_empty),
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                visibleBadges.take(4).forEach { badge ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        BadgeCard(badge = badge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                visibleBadges.drop(4).forEach { badge ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        BadgeCard(badge = badge)
                    }
                }
            }
        }

        if (badges.size > 8) {
            Spacer(modifier = Modifier.height(16.dp))
            SeeMoreLink(onClick = onSeeOthersClick)
        }
    }
}




@Composable
fun BadgeCard(
    badge: Badge,
    modifier: Modifier = Modifier
) {
    val icon = getBadgeIcon(badge)
    val bgColor = getBadgeColorForLevel(badge.level)
    val label = getBadgeLabel(badge)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (badge.isUnlocked) bgColor else Color.Gray)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (badge.category == BadgeCategory.EMOTION) {
                val emotion = EmotionDetected.entries.firstOrNull {
                    badge.badgeKey.contains(it.name, ignoreCase = true)
                }
                Text(
                    text = emotion?.toEmoji() ?: "❓",
                    fontSize = 22.sp,
                    modifier = Modifier.alpha(if (badge.isUnlocked) 1f else 0.3f)
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (badge.isUnlocked) Color.White else Color.LightGray,
                    modifier = Modifier
                        .size(28.dp)
                        .alpha(if (badge.isUnlocked) 1f else 0.3f)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 12.sp,
            maxLines = 2
        )
    }
}

@Composable
fun getBadgeLabel(badge: Badge): String {
    val parts = badge.badgeKey.split("_")
    if (parts.size < 2) return badge.badgeKey

    val category = badge.category
    val subType = parts.getOrNull(1)?.lowercase()?.replaceFirstChar { it.uppercaseChar() } ?: ""
    val count = parts.lastOrNull()?.toIntOrNull()

    val suffix = when (count) {
        1 -> stringResource(R.string.badge_suffix_1)
        null -> ""
        else -> "$count ${stringResource(R.string.badge_suffix_n)}"
    }

    return when (category) {
        BadgeCategory.NOTE -> "${stringResource(R.string.badge_prefix_note)} $suffix"
        BadgeCategory.QUESTION -> "${stringResource(R.string.badge_prefix_question)} $suffix"
        BadgeCategory.MESSAGE -> "${stringResource(R.string.badge_prefix_message)} $suffix"
        BadgeCategory.EMOTION -> {
            val emotion = EmotionDetected.entries.firstOrNull {
                it.name.equals(subType, ignoreCase = true)
            }
            val emotionStr = emotion?.getLabelRes()?.let { stringResource(it) } ?: subType
            "${stringResource(R.string.badge_prefix_emotion)} $emotionStr $suffix"
        }
    }.trim()
}

private fun getBadgeIcon(badge: Badge): ImageVector {
    return when (badge.category) {
        BadgeCategory.NOTE -> Icons.Outlined.Create
        BadgeCategory.EMOTION -> Icons.Outlined.Psychology
        BadgeCategory.MESSAGE ->  Icons.Outlined.ChatBubble
        BadgeCategory.QUESTION -> Icons.Outlined.QuestionMark
    }
}
