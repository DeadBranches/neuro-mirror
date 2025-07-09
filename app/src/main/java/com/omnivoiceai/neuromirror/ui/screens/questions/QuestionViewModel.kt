package com.omnivoiceai.neuromirror.ui.screens.questions

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.database.note.NoteWithQuestions
import com.omnivoiceai.neuromirror.data.database.question.QuestionWithDetails
import com.omnivoiceai.neuromirror.data.repositories.IntrospectionRepository
import com.omnivoiceai.neuromirror.data.repositories.NoteRepository
import com.omnivoiceai.neuromirror.data.repositories.QuestionRepository
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val questionRepository: QuestionRepository,
    private val noteRepository: NoteRepository,
    private val introspectionRepository: IntrospectionRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _questionsWithDetails = MutableStateFlow<List<QuestionWithDetails>>(emptyList())
    val questionsWithDetails = _questionsWithDetails.asStateFlow()

    suspend fun getNoteWithQuestions(noteId: Int): NoteWithQuestions {
        return noteRepository.getNoteWithQuestions(noteId)
    }
    
    suspend fun getQuestionsWithDetailsByNoteId(noteId: Int): List<QuestionWithDetails> {
        val questions = questionRepository.getQuestionsWithDetailsByNoteId(noteId)
        _questionsWithDetails.value = questions
        return questions
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
} 