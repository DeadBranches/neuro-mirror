package com.omnivoiceai.neuromirror.ui.screens.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.omnivoiceai.neuromirror.data.database.note.NoteWithQuestions
import com.omnivoiceai.neuromirror.data.database.question.MultipleChoiceQuestion
import com.omnivoiceai.neuromirror.data.database.question.OneShotQuestion
import com.omnivoiceai.neuromirror.data.database.question.Question
import com.omnivoiceai.neuromirror.data.database.question.QuestionType
import com.omnivoiceai.neuromirror.data.database.question.QuestionWithDetails
import com.omnivoiceai.neuromirror.data.repositories.NoteRepository
import com.omnivoiceai.neuromirror.data.repositories.QuestionRepository
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun NotesQuestionIntrospections(
    noteId: Int,
    noteRepository: NoteRepository,
    questionRepository: QuestionRepository? = null,
    navController: NavController? = null,
    modifier: Modifier = Modifier
) {
    var noteWithQuestions by remember { mutableStateOf<NoteWithQuestions?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var questionsWithDetails by remember { mutableStateOf<List<QuestionWithDetails>>(emptyList()) }
    
    LaunchedEffect(noteId) {
        withContext(Dispatchers.IO) {
            noteWithQuestions = noteRepository.getNoteWithQuestions(noteId)
            
            // If questionRepository is provided, get detailed questions
            if (questionRepository != null) {
                questionsWithDetails = questionRepository.getQuestionsWithDetailsByNoteId(noteId)
            }
            
            isLoading = false
        }
    }
    
    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 32.dp)
            )
            Text(
                text = "Loading questions...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        return
    }
    
    noteWithQuestions?.let { noteWithQ ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Note content
            Text(
                text = "Introspection",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = noteWithQ.note.content,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            // Questions
            Text(
                text = "Questions",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            noteWithQ.questions.forEach { question ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    // Find the question details from the list of detailed questions
                    val questionDetail = questionsWithDetails.find { it.question.id == question.id }
                    
                    when (question.type) {
                        QuestionType.Oneshot -> {
                            // Use the oneshot details from the database if available
                            val oneShotDetails = questionDetail?.oneShotData
                            if (oneShotDetails != null) {
                                OneShotQuestionComponent(
                                    question = question,
                                    oneShotQuestion = oneShotDetails,
                                    questionRepository = questionRepository!!,
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                // Fallback if details not found
                                DefaultQuestionComponent(question)
                            }
                        }
                        QuestionType.Multiple -> {
                            // Use the multiple choice details from the database if available
                            val multipleChoiceDetails = questionDetail?.multipleChoiceData
                            if (multipleChoiceDetails != null) {
                                MultipleChoiceQuestionComponent(
                                    question = question,
                                    multipleChoiceQuestion = multipleChoiceDetails,
                                    questionRepository = questionRepository!!,
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                // Fallback if details not found
                                DefaultQuestionComponent(question)
                            }
                        }
                        QuestionType.ShortText -> {
                            ShortTextQuestionComponent(
                                question = question,
                                questionRepository = questionRepository!!,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        QuestionType.LongText -> {
                            LongTextQuestionComponent(
                                question = question,
                                questionRepository = questionRepository!!,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
            
            // Chat button
            navController?.let {
                Button(
                    onClick = {
                        navController.navigate(NavigationRoute.ChatScreen(noteId))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text("Start Conversation")
                }
            }
        }
    } ?: run {
        // Handle case when note is not found
        Text(
            text = "Note not found",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun DefaultQuestionComponent(question: Question) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Question title
        question.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        // Question type
        Text(
            text = "Question type: ${question.type}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Helper functions to find question details
private fun findOneShotDetails(question: Question, noteWithQuestions: NoteWithQuestions): OneShotQuestion? {
    // In a real app, you would query the database for this
    // For this example, we're creating mock data based on the question
    return when (question.title) {
        "Did the absence of messages feel like a reflection of your worth?" -> 
            OneShotQuestion(questionId = question.id, answers = listOf("Yes", "No", "Uncertain"))
        else -> null
    }
}

private fun findMultipleChoiceDetails(question: Question, noteWithQuestions: NoteWithQuestions): MultipleChoiceQuestion? {
    // In a real app, you would query the database for this
    // For this example, we're creating mock data based on the question
    return when (question.title) {
        "What need was left unfulfilled by the silence?" -> 
            MultipleChoiceQuestion(
                questionId = question.id, 
                options = listOf("Reassurance", "Validation", "Companionship", "Distraction"),
                correctIndex = 1
            )
        else -> null
    }
} 