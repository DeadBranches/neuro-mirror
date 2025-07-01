package com.omnivoiceai.neuromirror.ui.screens.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.ui.screens.notes.components.NoteListItem

@Composable
fun ScrollableList(notes: List<Note>, navController: NavHostController) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(4.dp),
    ) {
        items(notes) {
            NoteListItem(it, navController)
        }
    }
}