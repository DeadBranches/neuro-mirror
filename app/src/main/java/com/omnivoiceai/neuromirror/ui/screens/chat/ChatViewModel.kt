package com.omnivoiceai.neuromirror.ui.screens.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omnivoiceai.neuromirror.data.database.note.NoteWithQuestions
import com.omnivoiceai.neuromirror.data.database.thread.Message
import com.omnivoiceai.neuromirror.data.database.thread.MessageRole
import com.omnivoiceai.neuromirror.data.database.thread.Thread
import com.omnivoiceai.neuromirror.data.repositories.BadgeRepository
import com.omnivoiceai.neuromirror.data.repositories.IntrospectionRepository
import com.omnivoiceai.neuromirror.data.repositories.NoteRepository
import com.omnivoiceai.neuromirror.data.repositories.QuestionRepository
import com.omnivoiceai.neuromirror.data.repositories.ThreadRepository
import com.omnivoiceai.neuromirror.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val isInitialized: Boolean = false,
    val currentMessage: String = "",
    val currentThreadId: Int? = null
)

interface ChatActions {
    fun initializeChat(context: Context, noteId: Int)
    fun sendMessage(context: Context, noteId: Int)
    fun updateCurrentMessage(message: String)
}

class ChatViewModel(
    private val introspectionRepository: IntrospectionRepository,
    private val noteRepository: NoteRepository,
    private val questionRepository: QuestionRepository,
    private val threadRepository: ThreadRepository,
    private val badgeRepository: BadgeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    val actions = object : ChatActions {
        override fun initializeChat(context: Context, noteId: Int) {
            if (_state.value.isInitialized) return
            
            viewModelScope.launch {
                try {
                    _state.value = _state.value.copy(isLoading = true)
                    
                    val noteWithQuestions = noteRepository.getNoteWithQuestions(noteId)
                    val questionsWithAnswers = questionRepository.getQuestionsWithAnswersByNoteId(noteId)
                    
                    // Check if there's an existing thread with messages for this note
                    val existingThreads = threadRepository.getThreadsWithMessagesByNoteId(noteId).first()
                    val currentThread = if (existingThreads.isNotEmpty()) {
                        // Use the most recent thread
                        existingThreads.first()
                    } else {
                        // Create new thread
                        val now = Date()
                        val thread = Thread(
                            noteId = noteId,
                            title = "Chat Session - ${java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault()).format(now)}",
                            date = now,
                            lastUpdated = now
                        )
                        val threadId = threadRepository.upsert(thread).toInt()
                        
                        // Build initial message and send to backend
                        val initialMessage = buildInitialMessage(noteWithQuestions, questionsWithAnswers)
                        
                        val response = introspectionRepository.sendMessage(
                            userMessage = initialMessage,
                            threadId = threadId.toString()
                        )
                        
                        // Save messages to database
                        threadRepository.upsert(Message(
                            threadId = threadId,
                            role = MessageRole.SYSTEM,
                            content = initialMessage,
                            timestamp = now
                        ))
                        
                        threadRepository.upsert(Message(
                            threadId = threadId,
                            role = MessageRole.ASSISTANT,
                            content = response.body,
                            timestamp = Date()
                        ))
                        
                        // Get the thread with messages
                        threadRepository.getThreadWithMessages(threadId)!!
                    }
                    
                    // Convert database messages to UI messages (excluding system messages)
                    val uiMessages = currentThread.messages
                        .filter { it.role != MessageRole.SYSTEM }
                        .map { dbMessage ->
                            ChatMessage(
                                content = dbMessage.content,
                                isUser = dbMessage.role == MessageRole.USER,
                                timestamp = dbMessage.timestamp.time
                            )
                        }
                    
                    _state.value = _state.value.copy(
                        messages = uiMessages,
                        currentThreadId = currentThread.thread.id,
                        isInitialized = true,
                        isLoading = false
                    )
                    
                } catch (e: Exception) {
                    Logger.error("Error initializing chat", e)
                    val errorMessage = ChatMessage(
                        content = "Sorry, there was an error starting the conversation.",
                        isUser = false
                    )
                    _state.value = _state.value.copy(
                        messages = listOf(errorMessage),
                        isInitialized = true,
                        isLoading = false
                    )
                }
            }
        }

        override fun sendMessage(context: Context, noteId: Int) {
            val currentState = _state.value
            val userMessage = currentState.currentMessage.trim()
            val threadId = currentState.currentThreadId
            
            if (userMessage.isBlank() || currentState.isLoading || threadId == null) return
            
            // Add user message immediately
            val userChatMessage = ChatMessage(
                content = userMessage,
                isUser = true
            )


            _state.value = currentState.copy(
                messages = currentState.messages + userChatMessage,
                currentMessage = "",
                isLoading = true
            )
            
            // Send to backend and save to database
            viewModelScope.launch {
                try {
                    val now = Date()
                    
                    // Save user message to database
                    threadRepository.upsert(Message(
                        threadId = threadId,
                        role = MessageRole.USER,
                        content = userMessage,
                        timestamp = now
                    ))

                    val response = introspectionRepository.sendMessage(
                        userMessage = userMessage,
                        threadId = threadId.toString()
                    )
                    
                    // Save assistant message to database
                    threadRepository.upsert(Message(
                        threadId = threadId,
                        role = MessageRole.ASSISTANT,
                        content = response.body,
                        timestamp = Date()
                    ))

                    // Add assistant response to UI
                    val assistantMessage = ChatMessage(
                        content = response.body,
                        isUser = false
                    )
                    
                    _state.value = _state.value.copy(
                        messages = _state.value.messages + assistantMessage,
                        isLoading = false
                    )
                } catch (e: Exception) {
                    Logger.error("Error sending message", e)
                    val errorMessage = ChatMessage(
                        content = "Sorry, there was an error sending your message.",
                        isUser = false
                    )
                    _state.value = _state.value.copy(
                        messages = _state.value.messages + errorMessage,
                        isLoading = false
                    )
                }
            }
        }

        override fun updateCurrentMessage(message: String) {
            _state.value = _state.value.copy(currentMessage = message)
        }
    }

    private fun buildInitialMessage(noteWithQuestions: NoteWithQuestions, questionsWithAnswers: List<com.omnivoiceai.neuromirror.data.database.question.QuestionWithAnswer>): String {
        val initialMessageBuilder = StringBuilder()
        initialMessageBuilder.append("Note content: ${noteWithQuestions.note.content}\n\n")
        
        // Add emotion detected
        noteWithQuestions.note.emotionDetected?.let { emotion ->
            initialMessageBuilder.append("Detected emotion: ${emotion.name}\n\n")
        }
        
        if (questionsWithAnswers.isNotEmpty()) {
            initialMessageBuilder.append("Questions and Answers:\n")
            questionsWithAnswers.forEach { qa ->
                qa.question.title?.let { title ->
                    initialMessageBuilder.append("\nQ: $title\n")
                    qa.answer?.let { answer ->
                        when {
                            !answer.answerText.isNullOrBlank() -> {
                                initialMessageBuilder.append("A: ${answer.answerText}\n")
                            }
                            !answer.selectedOptionText.isNullOrBlank() -> {
                                initialMessageBuilder.append("A: ${answer.selectedOptionText}\n")
                            }
                            else -> {
                                initialMessageBuilder.append("A: No answer provided\n")
                            }
                        }
                    } ?: run {
                        initialMessageBuilder.append("A: No answer provided\n")
                    }
                }
            }
        }
        
        return initialMessageBuilder.toString()
    }
} 