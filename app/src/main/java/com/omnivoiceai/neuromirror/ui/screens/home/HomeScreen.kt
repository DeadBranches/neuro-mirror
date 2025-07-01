package com.omnivoiceai.neuromirror.ui.screens.home

import android.content.res.Resources.Theme
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesState
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesViewModel
import com.omnivoiceai.neuromirror.ui.screens.notes.ScrollableList
import java.time.Instant
import java.util.Date

private const val TAG = "MainActivity"


private const val TAG2 = "GreetingComposable"
@Composable
fun HomeScreen(notesState:NotesState, notesViewModel: NotesViewModel, navController: NavHostController, modifier: Modifier = Modifier) {
//    Toast.makeText(LocalContext.current, "$TAG onPause", Toast.LENGTH_LONG).show()
    var text by remember { mutableStateOf("") }

    Column(
        modifier=Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "What happened today?",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),

        )
        EmptySpacer()
        TextField(
            value = text,
            modifier = Modifier
                .clip(RoundedCornerShape(15))
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            onValueChange = {text = it}
        )
        Row {
            Button(
                onClick = { text = "" },
                enabled = text.isNotEmpty()
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    notesViewModel::actions.get()
                        .addNote(Note( content = text, createdAt = Date.from(Instant.now())))
                    text = ""
                },
                enabled = text.isNotEmpty()
            ) {
                Text("Save")
            }
        }
        EmptySpacer()
        Text(
            "History",
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