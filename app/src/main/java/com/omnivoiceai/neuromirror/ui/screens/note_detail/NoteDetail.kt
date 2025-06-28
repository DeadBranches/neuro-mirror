package com.omnivoiceai.neuromirror.ui.screens.note_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.omnivoiceai.neuromirror.data.database.note.Note

@Composable
fun NoteDetailsScreen(note: Note, modifier: Modifier = Modifier){
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Text(note.content)
        Text(note.createdAt.toString())
        HorizontalDivider(thickness = 2.dp)
        Text("Emotion detected")
        note.emotionDetected?.name?.let { Text(it) }
        if(note.emotionDetected == null)
            Text("Not detected yet")
        HorizontalDivider(thickness = 2.dp)
        Button(onClick = { /*TODO*/ },
            Modifier.fillMaxWidth()
        ) {
            Text("Start Introspection 🧠")
        }
    }
}