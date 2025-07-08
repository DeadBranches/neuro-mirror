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
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Fireplace
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omnivoiceai.neuromirror.R

data class Badge(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val iconColor: androidx.compose.ui.graphics.Color,
    val isUnlocked: Boolean = true
)

@Composable
fun UserBadges(
    badges: List<Badge> = getDefaultBadges(),
    onSeeOthersClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.badges),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Mostra solo i primi 8 badge
        val visibleBadges = badges.take(8)
        
        // Prima riga (4 badge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            visibleBadges.take(4).forEach { badge ->
                BadgeCard(
                    badge = badge,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            visibleBadges.drop(4).take(4).forEach { badge ->
                BadgeCard(
                    badge = badge,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        if (badges.size > 8) {
            SeeMoreLink(
                onClick = onSeeOthersClick
            )
        }
    }
}

@Composable
fun BadgeCard(
    badge: Badge,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(badge.backgroundColor)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = badge.icon,
                contentDescription = badge.title,
                tint = badge.iconColor,
                modifier = Modifier.size(28.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = badge.title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 12.sp,
            maxLines = 2
        )
    }
}

// Dati base dei badge senza testi per evitare duplicazione
private data class BadgeData(
    val id: String,
    val stringRes: Int,
    val icon: ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color
)

private fun getBadgeData(): List<BadgeData> {
    return listOf(
        BadgeData(
            id = "first_note",
            stringRes = R.string.first_note,
            icon = Icons.Outlined.Create,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF4CAF50)
        ),
        BadgeData(
            id = "streak_3",
            stringRes = R.string.streak_of_3,
            icon = Icons.Outlined.Fireplace,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFFF9800)
        ),
        BadgeData(
            id = "week_warrior",
            stringRes = R.string.week_warrior,
            icon = Icons.Outlined.EmojiEvents,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF2196F3)
        ),
        BadgeData(
            id = "reflection_master",
            stringRes = R.string.reflection_master,
            icon = Icons.Outlined.Star,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF9C27B0)
        ),
        BadgeData(
            id = "mindful_moment",
            stringRes = R.string.mindful_moment,
            icon = Icons.Outlined.Psychology,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF607D8B)
        ),
        BadgeData(
            id = "positive_vibes",
            stringRes = R.string.positive_vibes,
            icon = Icons.Outlined.WbSunny,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFFFC107)
        ),
        BadgeData(
            id = "emotion_tracker",
            stringRes = R.string.emotion_tracker,
            icon = Icons.Outlined.Timeline,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF00BCD4)
        ),
        BadgeData(
            id = "self_care",
            stringRes = R.string.self_care,
            icon = Icons.Outlined.Favorite,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFE91E63)
        )
    )
}

@Composable
fun getAllBadges(): List<Badge> {
    return getBadgeData().map { badgeData ->
        Badge(
            id = badgeData.id,
            title = stringResource(badgeData.stringRes),
            icon = badgeData.icon,
            backgroundColor = badgeData.backgroundColor,
            iconColor = androidx.compose.ui.graphics.Color.White
        )
    }
}

@Composable
fun getDefaultBadges(): List<Badge> = getAllBadges() 