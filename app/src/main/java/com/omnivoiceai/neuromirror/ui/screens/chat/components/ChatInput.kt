package com.omnivoiceai.neuromirror.ui.screens.chat.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.omnivoiceai.neuromirror.ui.screens.chat.ChatState
import com.omnivoiceai.neuromirror.ui.screens.chat.ChatViewModel

@Composable
fun ChatInput(chatState: ChatState, chatViewModel: ChatViewModel, noteId: Int){
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        OutlinedTextField(
            value = chatState.currentMessage,
            onValueChange = chatViewModel.actions::updateCurrentMessage,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text("Type your message...") },
            maxLines = 3
        )

        IconButton(
            onClick = {
                chatViewModel.actions.sendMessage(context, noteId)
            },
            enabled = chatState.currentMessage.isNotBlank() && !chatState.isLoading
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
        }
    }
}