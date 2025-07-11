package com.omnivoiceai.neuromirror.ui.screens.questions

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.database.note.NoteWithQuestions
import com.omnivoiceai.neuromirror.data.database.question.QuestionWithDetails
import com.omnivoiceai.neuromirror.data.repositories.BadgeRepository
import com.omnivoiceai.neuromirror.data.repositories.IntrospectionRepository
import com.omnivoiceai.neuromirror.data.repositories.NoteRepository
import com.omnivoiceai.neuromirror.data.repositories.QuestionRepository
import com.omnivoiceai.neuromirror.data.repositories.ThreadRepository
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val questionRepository: QuestionRepository,
    private val noteRepository: NoteRepository,
    private val introspectionRepository: IntrospectionRepository,
    private val threadRepository: ThreadRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    private val _noteWithQuestions = MutableStateFlow<NoteWithQuestions?>(null)
    val noteWithQuestions: StateFlow<NoteWithQuestions?> = _noteWithQuestions.asStateFlow()
    private val _questionsWithDetails = MutableStateFlow<List<QuestionWithDetails>>(emptyList())
    val questionsWithDetails: StateFlow<List<QuestionWithDetails>> = _questionsWithDetails.asStateFlow()

    fun loadNoteAndQuestions(noteId: Int) {
        viewModelScope.launch {
            try {
                val note = noteRepository.getNoteWithQuestions(noteId)
                val questionDetails = questionRepository.getQuestionsWithDetailsByNoteId(noteId)

                _noteWithQuestions.value = note
                _questionsWithDetails.value = questionDetails
            } catch (e: Exception) {
                Logger.error("Failed to load note and questions", e)
            }
        }
    }

    fun generateQuestions(context: Context, note: Note, navController: NavController) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val noteWithQuestions = noteRepository.getNoteWithQuestions(note.id)

                if (noteWithQuestions.questions.isNotEmpty()) {
                    navController.navigate(NavigationRoute.NoteQuestionsScreen(note.id))
                    return@launch
                }

                navController.navigate(NavigationRoute.LoadingScreen(note.id))

                val response = introspectionRepository.sendQuestion(
                    userMessage = note.content,
                    threadId = note.id.toString()
                )

                questionRepository.saveQuestions(response.questions, note.id)

                val updatedNote = note.copy(isEvaluated = true)
                noteRepository.upsert(updatedNote)

                badgeRepository.checkQuestionMilestones(questionRepository.getAllGrouped())

                navController.navigate(NavigationRoute.NoteQuestionsScreen(note.id)) {
                    popUpTo(NavigationRoute.LoadingScreen(note.id)) { inclusive = true }
                }
            } catch (e: Exception) {
                Logger.error("Error generating questions", e)
                navController.popBackStack()
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getShortAnswer(questionId: Int): String? {
        return questionRepository.getLatestAnswerForQuestion(questionId)?.answerText
    }

    suspend fun saveShortAnswer(questionId: Int, text: String) {
        questionRepository.saveAnswer(questionId = questionId, answerText = text)
    }

    suspend fun getSelectedOptionIndex(questionId: Int): Int? {
        return questionRepository.getLatestAnswerForQuestion(questionId)?.selectedOptionIndex
    }

    suspend fun saveSelectedOption(questionId: Int, index: Int, text: String) {
        questionRepository.saveAnswer(questionId = questionId, selectedOptionIndex = index, selectedOptionText = text)
    }

    suspend fun getLongAnswer(questionId: Int): String? {
        return questionRepository.getLatestAnswerForQuestion(questionId)?.answerText
    }

    suspend fun saveLongAnswer(questionId: Int, text: String) {
        questionRepository.saveAnswer(questionId = questionId, answerText = text)
    }

    suspend fun getOneShotAnswer(questionId: Int): String? {
        return questionRepository.getLatestAnswerForQuestion(questionId)?.selectedOptionText
    }

    suspend fun saveOneShotAnswer(questionId: Int, answer: String) {
        questionRepository.saveAnswer(
            questionId = questionId,
            selectedOptionText = answer
        )
    }


    private val _hasConversation = MutableStateFlow(false)
    val hasConversation: StateFlow<Boolean> = _hasConversation.asStateFlow()

    fun checkConversationExists(noteId: Int) {
        viewModelScope.launch {
            threadRepository.getThreadsWithMessagesByNoteId(noteId)
                .collect { threads ->
                    _hasConversation.value = threads.isNotEmpty()
                }
        }
    }

}
