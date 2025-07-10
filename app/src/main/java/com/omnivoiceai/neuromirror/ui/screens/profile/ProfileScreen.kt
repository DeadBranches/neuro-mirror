package com.omnivoiceai.neuromirror.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesViewModel
import com.omnivoiceai.neuromirror.ui.screens.profile.components.BadgeCard
import com.omnivoiceai.neuromirror.ui.screens.profile.components.CameraIconButton
import com.omnivoiceai.neuromirror.ui.screens.profile.components.CircleAvatar
import com.omnivoiceai.neuromirror.ui.screens.profile.components.EmotionRadar
import com.omnivoiceai.neuromirror.ui.screens.profile.components.GenericBottomSheet
import com.omnivoiceai.neuromirror.ui.screens.profile.components.LabelWithEdit
import com.omnivoiceai.neuromirror.ui.screens.profile.components.SeeMoreLink
import com.omnivoiceai.neuromirror.ui.screens.profile.components.Size
import com.omnivoiceai.neuromirror.ui.screens.profile.components.TimelineItem
import com.omnivoiceai.neuromirror.ui.screens.profile.components.UserBadges

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, notesViewModel: NotesViewModel, badgeViewModel: BadgeViewModel, navController: NavHostController, modifier: Modifier = Modifier){
    var showTimelineBottomSheet by remember { mutableStateOf(false) }
    var showBadgesBottomSheet by remember { mutableStateOf(false) }
    val notes = notesViewModel.state.collectAsStateWithLifecycle()
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
            UserBadges(
                badges = badgeViewModel.badges.collectAsStateWithLifecycle().value,
                onSeeOthersClick = {
                    showBadgesBottomSheet = true
                }
            )
            EmptySpacer(height = 32.dp)
            Text(stringResource(R.string.emotion_radar))
            EmotionRadar(notesViewModel = notesViewModel)
            EmptySpacer(height = 32.dp)
        }

        item {
            Text(stringResource(R.string.emotion_timeline))
        }
        
        val filteredNotes = notes.value.notes.filter { it.emotionDetected != null }
        val visibleNotes = filteredNotes.take(5)
        if(visibleNotes.isEmpty()) {
            item {
                Text(stringResource(R.string.emotion_timeline_empty), modifier = Modifier.padding(vertical = 16.dp))
            }
        }

        if(visibleNotes.isNotEmpty())
        item {
            EmptySpacer(height = 32.dp)
        }

        itemsIndexed(visibleNotes) { index, note ->
            TimelineItem(
                note = note,
                isLeft = index % 2 == 0
            )
        }
        
        if (filteredNotes.size > 5) {
            item {
                SeeMoreLink(
                    text = stringResource(R.string.see_full_timeline),
                    onClick = {
                        showTimelineBottomSheet = true
                    }
                )
            }
        }
    }
    
    // BottomSheet per la timeline completa
    GenericBottomSheet(
        showBottomSheet = showTimelineBottomSheet,
        onDismiss = { showTimelineBottomSheet = false },
        title = stringResource(R.string.emotion_timeline),
        heightFraction = 0.85f // Grande per mostrare molte note
    ) {
        val filteredNotes = notes.value.notes.filter { it.emotionDetected != null }
        
        if (filteredNotes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_emotions_recorded),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                itemsIndexed(filteredNotes) { index, note ->
                    TimelineItem(
                        note = note,
                        isLeft = index % 2 == 0
                    )
                }
            }
        }
    }
    
    GenericBottomSheet(
        showBottomSheet = showBadgesBottomSheet,
        onDismiss = { showBadgesBottomSheet = false },
        title = stringResource(R.string.all_badges),
        heightFraction = 0.85f
    ) {
        val allBadges = badgeViewModel.badges.collectAsStateWithLifecycle().value
        
        if (allBadges.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_badges_available),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(allBadges) { badge ->
                    BadgeCard(badge = badge)
                }
            }
        }
    }
}