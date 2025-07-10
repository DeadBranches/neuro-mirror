package com.omnivoiceai.neuromirror.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omnivoiceai.neuromirror.data.database.note.EmotionDetected
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.database.note.toEmoji


@Composable
fun TimelineItem(note: Note, isLeft: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(120.dp)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                .align(Alignment.Center)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLeft) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp), // Padding generale a sinistra
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TimelineContent(
                        note = note,
                        modifier = Modifier.padding(end = 30.dp),
                        alignment = Alignment.End
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            } else {
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp), // Padding generale a destra
                    contentAlignment = Alignment.CenterStart
                ) {
                    TimelineContent(
                        note = note,
                        modifier = Modifier.padding(start = 30.dp),
                        alignment = Alignment.Start
                    )
                }
            }
        }

        EmotionDivider(note.emotionDetected!!)
    }
}

@Composable
fun BoxScope.EmotionDivider (emotionDetected: EmotionDetected){
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                2.dp,
                MaterialTheme.colorScheme.primary,
                CircleShape
            )
            .align(Alignment.Center),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emotionDetected.toEmoji(),
            fontSize = 18.sp
        )
    }
}

@Composable
fun TimelineContent(
    note: Note,
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            note.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = note.content,
                maxLines = 2,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault()).format(note.createdAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
