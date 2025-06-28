package com.omnivoiceai.neuromirror.ui.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesState
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesViewModel
import java.time.Instant
import java.util.Date

private const val TAG = "MainActivity"


private const val TAG2 = "GreetingComposable"
@Composable
fun HomeScreen(notesState:NotesState, notesViewModel: NotesViewModel, navController: NavHostController, modifier: Modifier = Modifier) {
//    Toast.makeText(LocalContext.current, "$TAG onPause", Toast.LENGTH_LONG).show()
    var text by remember { mutableStateOf("") }

    Column(
        modifier=Modifier.fillMaxSize()
    ) {
        Text(
            "What happened today?",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        TextField(
            value = text,
            onValueChange = {text = it}
        )
        Row {
            Button(onClick = { text = "" }) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    notesViewModel::actions.get()
                        .addNote(Note( content = text, createdAt = Date.from(Instant.now())))
                },
                enabled = text.isNotEmpty()
            ) {
                Text("Save")
            }
        }
        Text(
            "History",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        ScrollableList(notes=notesState.notes)
    }


//    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
//        Log.i(
//            TAG2,
//            "onCreate")
//    }
//
//    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
//        Log.i(
//            TAG2,
//            "onResume")
//    }
}

@Composable
fun ScrollableList(notes: List<Note>) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(notes) {
            MaterialListItem(it)
        }
    }
}

@Composable
fun MaterialListItem(note: Note, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxWidth()
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
