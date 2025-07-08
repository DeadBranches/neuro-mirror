package com.omnivoiceai.neuromirror.ui.screens.profile.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.omnivoiceai.neuromirror.utils.Logger

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
) {
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
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (!edit) {
            Text(
                field.ifEmpty { hintText }, 
                modifier = Modifier.clickable { edit = true }
            )
            Icon(
                Icons.Outlined.Edit, 
                contentDescription = "Edit", 
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .absoluteOffset(y = 0.dp)
                    .clickable { edit = true }
            )
        } else {
            val keyboardController = LocalSoftwareKeyboardController.current
            TextField(
                value = field,
                onValueChange = { field = it; if (updateOnChange) onSave(field) },
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
                        if (!updateOnChange)
                            onSave(field)
                        keyboardController?.hide()
                    }
                ),
                visualTransformation = if (isPassword) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}