package com.omnivoiceai.neuromirror.ui.screens.notes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute

@Composable
fun NoteListItem(note: Note, navController: NavHostController, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(15))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxWidth()
            .clickable { navController.navigate(NavigationRoute.NoteDetailsScreen(id = note.id)) }
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Android Logo",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.size(16.dp))
        Text(
            note.content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            note.createdAt.toString(),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
