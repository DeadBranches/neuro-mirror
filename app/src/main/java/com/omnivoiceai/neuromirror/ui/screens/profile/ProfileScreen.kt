package com.omnivoiceai.neuromirror.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesState
import com.omnivoiceai.neuromirror.ui.screens.profile.components.CameraIconButton
import com.omnivoiceai.neuromirror.ui.screens.profile.components.CircleAvatar
import com.omnivoiceai.neuromirror.ui.screens.profile.components.EmotionRadar
import com.omnivoiceai.neuromirror.ui.screens.profile.components.LabelWithEdit
import com.omnivoiceai.neuromirror.ui.screens.profile.components.Size
import com.omnivoiceai.neuromirror.ui.screens.profile.components.TimelineItem

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, notesState: NotesState, navController: NavHostController, modifier: Modifier = Modifier){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
//            .verticalScroll(scroll),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box {
                CircleAvatar(image = viewModel.state.imageUrl, size = Size.Xl)
                CameraIconButton(
                    onImageSelected = viewModel::setImageUrl,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }

            EmptySpacer()
            LabelWithEdit(viewModel.state.username, viewModel::setUsername, updateOnChange = false, hintText = stringResource(R.string.username))

        }
        item {
            Row {
                LabelWithEdit(viewModel.state.firstName, viewModel::setFirstName, hintText = stringResource(R.string.first_name))
                LabelWithEdit(viewModel.state.lastName, viewModel::setLastName, hintText = stringResource(R.string.last_name))
            }
            EmptySpacer(height = 32.dp)
            Text(
                stringResource(R.string.badges))
            EmptySpacer(height = 32.dp)
            Text(stringResource(R.string.emotion_radar))
            EmotionRadar()
            EmptySpacer(height = 32.dp)
        }

        item {
            Text(stringResource(R.string.emotion_timeline))
        }
        
        val filteredNotes = notesState.notes.filter { it.emotionDetected != null }
        itemsIndexed(filteredNotes) { index, note ->
            TimelineItem(
                note = note,
                isLeft = index % 2 == 0
            )
        }
    }
}