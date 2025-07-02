package com.omnivoiceai.neuromirror.ui.screens.note_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.database.note.toEmoji
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.ui.screens.notes.components.NoteDate

@Composable
fun NoteDetailsScreen(note: Note, modifier: Modifier = Modifier){
    Column(
        modifier = Modifier.fillMaxSize().padding(0.dp, 16.dp)
    ){
        Text(note.content, modifier = Modifier.padding(16.dp, 0.dp))
        EmptySpacer()
        NoteDate(note = note, modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp))
        EmptySpacer()
        HorizontalDivider(thickness = 2.dp)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 32.dp)
        ) {
            Text("Emotion detected")
            note.emotionDetected?.let {
                Text(
                    it.toEmoji(),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(8.dp)
                )
                Text(it.name)
            }
            if(note.emotionDetected == null)
                Text("Not detected yet", modifier = Modifier.padding(16.dp, 0.dp))
        }
        HorizontalDivider(thickness = 2.dp)
        Button(onClick = { /*TODO*/ },
            Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
        ) {
            Text("Start Introspection 🧠")
        }
    }
}