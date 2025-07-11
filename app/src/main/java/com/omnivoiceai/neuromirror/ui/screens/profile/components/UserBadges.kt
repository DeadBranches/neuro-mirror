package com.omnivoiceai.neuromirror.ui.screens.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.badge.Badge

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