package com.omnivoiceai.neuromirror.ui.screens.note_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
        Text(note.content, modifier = Modifier.padding(16.dp, 0.dp))
        Text(note.createdAt.toString(), modifier = Modifier.padding(16.dp, 0.dp))
        HorizontalDivider(thickness = 2.dp)
        Text("Emotion detected", modifier = Modifier.padding(16.dp, 0.dp))
        note.emotionDetected?.name?.let { Text(it) }
        if(note.emotionDetected == null)
            Text("Not detected yet", modifier = Modifier.padding(16.dp, 0.dp))
        HorizontalDivider(thickness = 2.dp)
        Button(onClick = { /*TODO*/ },
            Modifier.fillMaxWidth().padding(16.dp, 8.dp)
        ) {
            Text("Start Introspection 🧠")
        }
    }
}