package com.omnivoiceai.neuromirror.ui.screens.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesState
import com.omnivoiceai.neuromirror.ui.screens.profile.components.EmotionRadar
import com.omnivoiceai.neuromirror.ui.screens.profile.components.TimelineItem
import com.omnivoiceai.neuromirror.utils.Logger
import com.omnivoiceai.neuromirror.utils.rememberCameraLauncher

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, notesState: NotesState, navController: NavHostController ,modifier: Modifier = Modifier){
    val cameraLauncher = rememberCameraLauncher(
        onPictureTaken = { imageUri -> viewModel.setImageUrl(imageUri.toString()) }
    )
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
                IconButton (
                    onClick = cameraLauncher::captureImage,
//                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .size(32.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        Icons.Outlined.PhotoCamera,
                        contentDescription = "Camera icon",
                        modifier = Modifier.fillMaxSize().padding(8.dp)
                    )
                }
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
        
        // Timeline items individuali per performance lazy
        val filteredNotes = notesState.notes.filter { it.emotionDetected != null }
        itemsIndexed(filteredNotes) { index, note ->
            TimelineItem(
                note = note,
                isLeft = index % 2 == 0
            )
        }
    }
}

enum class ImageType { Local, Network }
enum class Size { S, M, Xl, XXL }

@Composable
fun CircleAvatar(image: String, modifier: Modifier = Modifier, type: ImageType = ImageType.Network, size: Size = Size.M, description: String = "Description"){
    Logger.info(image);
    val imageSize = when (size) {
        Size.S -> 32.dp
        Size.M -> 64.dp
        Size.Xl -> 92.dp
        Size.XXL -> 128.dp
        else -> 64.dp
    }
    if(type == ImageType.Local || image.isEmpty()){
        Image(
            Icons.Outlined.Image,
            description,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        )
    } else {
        AsyncImage(
            ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .build(),
            contentDescription = description,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(imageSize)
                .clip(CircleShape)
        )
    }
}


@Composable
fun LabelWithEdit(
    initText: String,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    updateOnChange: Boolean = true,
    isEditingMode: Boolean = false,
    hintText: String = "",
){
    var edit by rememberSaveable { mutableStateOf(false) }
    var field by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(initText, isEditingMode) {
        if (isEditingMode) {
            edit = true
            if (field.isBlank()) {
                field = initText
            }
        } else if (!edit) {
            field = initText
        }
    }

    if (edit) {
        BackHandler {
            edit = false
        }
    }

    Box(
        modifier = modifier
            .size(
                LocalConfiguration.current.screenWidthDp.dp / 2, height = 64.dp
            )
//            .background(MaterialTheme.colorScheme.surfaceDim)
        ,
        contentAlignment = Alignment.Center,
    ){
        if(!edit) {
            Text(field.ifEmpty { hintText }, modifier = Modifier.clickable { edit = true })
            Icon(Icons.Outlined.Edit, contentDescription = "Edit", modifier = Modifier
                .align(Alignment.TopEnd)
                .absoluteOffset(y = 0.dp)
                .clickable { edit = true }
            )
        }
        else {
            val keyboardController = LocalSoftwareKeyboardController.current
            TextField(
                value = field,
                onValueChange = { field = it; if(updateOnChange) onSave(field)  },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = keyboardType
                ),
                placeholder = {
                    Text(text = hintText)
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        Logger.info("Saving $field")
                        edit = false
                        if(!updateOnChange)
                            onSave(field)
                        keyboardController?.hide()
                    }
                ),
                visualTransformation = isPassword.let{
                    if(it) PasswordVisualTransformation()
                    else VisualTransformation.None
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}