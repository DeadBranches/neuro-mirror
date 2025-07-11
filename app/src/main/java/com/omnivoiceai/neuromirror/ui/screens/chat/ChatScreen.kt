package com.omnivoiceai.neuromirror.ui.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.ui.screens.chat.components.ChatInput
import com.omnivoiceai.neuromirror.ui.screens.chat.components.MessageBubble

@Composable
fun ChatScreen(
    noteId: Int,
    chatViewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val chatState by chatViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    // Initialize chat when screen loads
    LaunchedEffect(noteId) {
        chatViewModel.actions.reset()
        chatViewModel.actions.initializeChat(context, noteId)
    }

    // Auto-scroll to bottom when new messages are added
    LaunchedEffect(chatState.messages.size) {
        if (chatState.messages.isNotEmpty()) {
            listState.animateScrollToItem(chatState.messages.size - 1)
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chatState.messages) { message ->
                if(message == chatState.messages.first()){
                    EmptySpacer()
                }
                MessageBubble(
                    message = message,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            
            if (chatState.isLoading) {
                item {
                    if(chatState.messages.count() == 0){
                        EmptySpacer()
                    }
                    MessageBubble(
                        message = ChatMessage(content = "", isUser = false),
                        isLoading = true
                    )
                }
            }
        }

        ChatInput(chatState=chatState, chatViewModel=chatViewModel, noteId=noteId)
    }
}