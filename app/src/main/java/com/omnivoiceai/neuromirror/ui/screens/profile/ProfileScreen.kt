package com.omnivoiceai.neuromirror.ui.screens.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.utils.Logger

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavHostController ,modifier: Modifier = Modifier){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircleAvatar(image = "", size = Size.Xl)
        EmptySpacer()
        LabelWithEdit(viewModel.state.username, viewModel::setUsername, updateOnChange = false, hintText = stringResource(R.string.username))
        Row {
            LabelWithEdit(viewModel.state.firstName, viewModel::setFirstName, hintText = stringResource(R.string.first_name))
            LabelWithEdit(viewModel.state.lastName, viewModel::setLastName, hintText = stringResource(R.string.last_name))
        }
        EmptySpacer(height = 32.dp)
        Text(
            stringResource(R.string.badges))
        EmptySpacer(height = 32.dp)
        Text(stringResource(R.string.emotion_radar))
        EmptySpacer(height = 32.dp)
        Text(stringResource(R.string.emotion_timeline))
    }
}

enum class ImageType { Local, Network }
enum class Size { S, M, Xl, XXL }

@Composable
fun CircleAvatar(image: String, modifier: Modifier = Modifier, type: ImageType = ImageType.Network, size: Size = Size.M, description: String = "Description"){
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
//    val size = 64.dp

        AsyncImage(
            model = "https://www.linkiesta.it/wp-content/uploads/2023/03/random-linkiesta.jpg",
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