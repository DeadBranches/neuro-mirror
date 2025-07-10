package com.omnivoiceai.neuromirror.ui.screens.note_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.note.toEmoji
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.ui.screens.notes.components.NoteDate
import com.omnivoiceai.neuromirror.ui.screens.questions.QuestionViewModel

@Composable
fun NoteDetailsScreen(
    noteId: Int,
    questionViewModel: QuestionViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val noteWithQuestions by questionViewModel.noteWithQuestions.collectAsState()

    LaunchedEffect(noteId) {
        questionViewModel.loadNoteDetails(noteId)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        questionViewModel.loadNoteDetails(noteId)
    }

    noteWithQuestions?.let { data ->
        val note = data.note
        val isEvaluated = noteWithQuestions?.questions?.isNotEmpty() == true

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(note.content, modifier = Modifier.padding(top = 16.dp))
            EmptySpacer()
            NoteDate(note = note, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp))
            EmptySpacer()
            HorizontalDivider(thickness = 2.dp)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            ) {
                Text(stringResource(R.string.emotion_detected))

                note.emotionDetected?.let {
                    Text(
                        it.toEmoji(),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(stringResource(it.getLabelRes()))
                } ?: Text(
                    text = stringResource(R.string.emotion_detected),
                    modifier = Modifier.padding(16.dp)
                )
            }

            HorizontalDivider(thickness = 2.dp)

            Button(
                onClick = {
                    questionViewModel.generateQuestions(context, note, navController)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = if (!isEvaluated)
                        stringResource(R.string.emotion_start_introspection)
                    else
                        stringResource(R.string.emotion_continue_introspection)
                )
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
