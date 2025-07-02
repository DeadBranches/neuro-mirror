package com.omnivoiceai.neuromirror.ui.screens.notes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.database.note.toEmoji
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.utils.toFormattedLines

@Composable
fun NoteListItem(note: Note, navController: NavHostController, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(15))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxWidth()
            .clickable { navController.navigate(NavigationRoute.NoteDetailsScreen(id = note.id)) }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        note.emotionDetected.let {
            if(it == null)
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Android Logo",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.size(48.dp)
            )
            else Text(it.toEmoji())
        }

        Spacer(Modifier.size(16.dp))
        Text(
            note.content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
        )
        Spacer(Modifier.size(8.dp))
        NoteDate(note = note)
    }
}


@Composable
fun NoteDate(note: Note, modifier: Modifier = Modifier, alignment: Alignment.Horizontal = Alignment.End){
    val (day, time) = note.createdAt.toFormattedLines()
    Column(horizontalAlignment = alignment, modifier = modifier) {
        Text(
            text = day,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = time,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
