package com.omnivoiceai.neuromirror.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.ui.components.forms.MultilineTextField
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesState
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesViewModel
import com.omnivoiceai.neuromirror.ui.screens.notes.ScrollableList
import java.time.Instant
import java.util.Date


@Composable
fun HomeScreen(notesState:NotesState, notesViewModel: NotesViewModel, navController: NavHostController, modifier: Modifier = Modifier) {
//    Toast.makeText(LocalContext.current, "$TAG onPause", Toast.LENGTH_LONG).show()
    var text by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.home_page_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),

        )
        EmptySpacer()

        MultilineTextField(text, onValueChange = {text = it})

        Row {
            Button(
                onClick = { text = TextFieldValue() },
                enabled = text.text.isNotEmpty()
            ) {
                Text(stringResource(R.string.cancel),
                )
            }
            EmptySpacer(width = 8.dp)
            Button(
                onClick = {
                    notesViewModel::actions.get()
                        .addNote(Note( content = text.text, createdAt = Date.from(Instant.now())))
                    text = TextFieldValue()
                },
                enabled = text.text.isNotEmpty()
            ) {
                Text(stringResource(R.string.save))
            }
        }
        EmptySpacer()
        Text(
            stringResource(R.string.home_page_history),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )
        ScrollableList(notes=notesState.notes, navController=navController)
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