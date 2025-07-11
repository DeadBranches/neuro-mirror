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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    fun reset()
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
//            if (_state.value.isInitialized) return

            viewModelScope.launch {
                _state.value = _state.value.copy(isLoading = true)

                try {
                    val noteWithQuestions: NoteWithQuestions
                    val questionsWithAnswers: List<com.omnivoiceai.neuromirror.data.database.question.QuestionWithAnswer>
                    val currentThread: com.omnivoiceai.neuromirror.data.database.thread.ThreadWithMessages

                    withContext(Dispatchers.IO) {
                        noteWithQuestions = noteRepository.getNoteWithQuestions(noteId)
                        questionsWithAnswers = questionRepository.getQuestionsWithAnswersByNoteId(noteId)
                        val existingThreads = threadRepository.getThreadsWithMessagesByNoteId(noteId).first()

                        currentThread = if (existingThreads.isNotEmpty()) {
                            existingThreads.first()
                        } else {
                            val now = Date()
                            val thread = Thread(
                                noteId = noteId,
                                title = "Chat Session - ${SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(now)}",
                                date = now,
                                lastUpdated = now
                            )
                            val threadId = threadRepository.upsert(thread).toInt()

                            val initialMessage = buildInitialMessage(noteWithQuestions, questionsWithAnswers)

                            val response = introspectionRepository.sendMessage(
                                userMessage = initialMessage,
                                threadId = threadId.toString()
                            )

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

                            threadRepository.getThreadWithMessages(threadId)
                                ?: throw IllegalStateException("Thread not found after creation")
                        }
                    }

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
                    _state.value = _state.value.copy(
                        messages = listOf(ChatMessage(
                            content = "Sorry, there was an error starting the conversation.",
                            isUser = false
                        )),
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
            val userChatMessage = ChatMessage(content = userMessage, isUser = true)

            _state.value = currentState.copy(
                messages = currentState.messages + userChatMessage,
                currentMessage = "",
                isLoading = true
            )

            viewModelScope.launch {
                try {
                    val now = Date()
                    val responseBody = withContext(Dispatchers.IO) {
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

                        threadRepository.upsert(Message(
                            threadId = threadId,
                            role = MessageRole.ASSISTANT,
                            content = response.body,
                            timestamp = Date()
                        ))

                        response.body
                    }

                    val assistantMessage = ChatMessage(content = responseBody, isUser = false)

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

        override fun reset() {
            _state.value = ChatState()
        }
    }

    private fun buildInitialMessage(
        noteWithQuestions: NoteWithQuestions,
        questionsWithAnswers: List<com.omnivoiceai.neuromirror.data.database.question.QuestionWithAnswer>
    ): String {
        val initialMessageBuilder = StringBuilder()
        initialMessageBuilder.append("Note content: ${noteWithQuestions.note.content}\n\n")

        noteWithQuestions.note.emotionDetected?.let { emotion ->
            initialMessageBuilder.append("Detected emotion: ${emotion.name}\n\n")
        }

        if (questionsWithAnswers.isNotEmpty()) {
            initialMessageBuilder.append("Questions and Answers:\n")
            questionsWithAnswers.forEach { qa ->
                qa.question.title?.let { title ->
                    initialMessageBuilder.append("\nQ: $title\n")
                    val answerText = qa.answer?.answerText
                    val selectedOption = qa.answer?.selectedOptionText

                    initialMessageBuilder.append(
                        when {
                            !answerText.isNullOrBlank() -> "A: $answerText\n"
                            !selectedOption.isNullOrBlank() -> "A: $selectedOption\n"
                            else -> "A: No answer provided\n"
                        }
                    )
                }
            }
        }

        return initialMessageBuilder.toString()
    }
}
