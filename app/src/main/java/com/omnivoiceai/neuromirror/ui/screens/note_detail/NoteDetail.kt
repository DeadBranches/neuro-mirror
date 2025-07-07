package com.omnivoiceai.neuromirror.ui.screens.note_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.database.note.toEmoji
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.ui.screens.notes.components.NoteDate
import com.omnivoiceai.neuromirror.ui.screens.questions.QuestionViewModel
import com.omnivoiceai.neuromirror.utils.Logger

@Composable
fun NoteDetailsScreen(
    note: Note, 
    emotionViewModel: EmotionViewModel,
    questionViewModel: QuestionViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    var currentNote by remember { mutableStateOf(note) }
    
    LaunchedEffect(note.id) {
        try {
            val noteWithQuestions = questionViewModel.getNoteWithQuestions(note.id)
            if (noteWithQuestions.questions.isNotEmpty() && !currentNote.isEvaluated) {
                currentNote = currentNote.copy(isEvaluated = true)
            }
        } catch (e: Exception) {
            Logger.error("Error: ", e)
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(0.dp, 16.dp)
    ){
        Text(currentNote.content, modifier = Modifier.padding(16.dp, 0.dp))
        EmptySpacer()
        NoteDate(note = currentNote, modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp))
        EmptySpacer()
        HorizontalDivider(thickness = 2.dp)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 32.dp)
        ) {
            Text("Emotion detected")
            currentNote.emotionDetected?.let {
                Text(
                    it.toEmoji(),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(8.dp)
                )
                Text(it.name)
            }
            if(currentNote.emotionDetected == null)
                Text("Not detected yet", modifier = Modifier.padding(16.dp, 0.dp))
        }
        HorizontalDivider(thickness = 2.dp)
        Button(onClick = {
            questionViewModel.generateQuestions(context, currentNote, navController)
        },
            Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
        ) {
            if (currentNote.isEvaluated) {
                Text("View Introspection Questions 🧠")
            } else {
                Text("Start Introspection 🧠")
            }
        }
    }
}