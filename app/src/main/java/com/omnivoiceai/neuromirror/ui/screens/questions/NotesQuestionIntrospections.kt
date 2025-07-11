package com.omnivoiceai.neuromirror.ui.screens.questions

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.question.QuestionType
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.screens.questions.components.DefaultQuestionComponent
import com.omnivoiceai.neuromirror.ui.screens.questions.components.LongTextQuestionComponent
import com.omnivoiceai.neuromirror.ui.screens.questions.components.MultipleChoiceQuestionComponent
import com.omnivoiceai.neuromirror.ui.screens.questions.components.OneShotQuestionComponent
import com.omnivoiceai.neuromirror.ui.screens.questions.components.ShortTextQuestionComponent

@Composable
fun NotesQuestionIntrospections(
    noteId: Int,
    viewModel: QuestionViewModel,
    navController: NavController? = null,
    modifier: Modifier = Modifier
) {
    val noteWithQuestions by viewModel.noteWithQuestions.collectAsState()
    val questionsWithDetails by viewModel.questionsWithDetails.collectAsState()


    LaunchedEffect(noteId) {
        viewModel.loadNoteAndQuestions(noteId)
    }

    val hasConversation by viewModel.hasConversation.collectAsState()

    LaunchedEffect(noteId) {
        viewModel.checkConversationExists(noteId)
    }

    if (noteWithQuestions == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Note content
            Text(
                text = stringResource(R.string.introspection),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
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
                text = stringResource(R.string.question),
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
                            val oneShotDetails = questionDetail?.oneShotData
                            if (oneShotDetails != null) {
                                OneShotQuestionComponent(
                                    question = question,
                                    oneShotQuestion = oneShotDetails,
                                    onLoadAnswer = { id -> viewModel.getOneShotAnswer(id) },
                                    onSaveAnswer = { id, answer ->
                                        viewModel.saveOneShotAnswer(
                                            id,
                                            answer
                                        )
                                    },
                                    modifier = Modifier.padding(16.dp)
                                )

                            } else {
                                DefaultQuestionComponent(question)
                            }
                        }

                        QuestionType.Multiple -> {
                            val multipleChoiceDetails = questionDetail?.multipleChoiceData
                            if (multipleChoiceDetails != null) {
                                MultipleChoiceQuestionComponent(
                                    question = question,
                                    multipleChoiceQuestion = multipleChoiceDetails,
                                    onLoadAnswer = { id -> viewModel.getSelectedOptionIndex(id) },
                                    onSaveAnswer = { id, index, text ->
                                        viewModel.saveSelectedOption(
                                            id,
                                            index,
                                            text
                                        )
                                    },
                                    modifier = Modifier.padding(16.dp)
                                )

                            } else {
                                DefaultQuestionComponent(question)
                            }
                        }

                        QuestionType.ShortText -> {
                            ShortTextQuestionComponent(
                                question = question,
                                onLoadAnswer = { id -> viewModel.getShortAnswer(id) },
                                onSaveAnswer = { id, text -> viewModel.saveShortAnswer(id, text) },
                                modifier = Modifier.padding(16.dp)
                            )

                        }

                        QuestionType.LongText -> {
                            LongTextQuestionComponent(
                                question = question,
                                onLoadAnswer = { id -> viewModel.getLongAnswer(id) },
                                onSaveAnswer = { id, text -> viewModel.saveLongAnswer(id, text) },
                                modifier = Modifier.padding(16.dp)
                            )

                        }
                    }

                }
            }
            
            navController?.let {
                    Button(
                        onClick = {
                            navController.navigate(NavigationRoute.ChatScreen(noteId))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    ) {
                        if(!hasConversation){
                            Text( stringResource(R.string.start_conversation))
                        } else {
                            Text( stringResource(R.string.continue_conversation))
                        }
                    }
            }
        }
    } ?: run {
        Text(
            text = "Note not found",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}