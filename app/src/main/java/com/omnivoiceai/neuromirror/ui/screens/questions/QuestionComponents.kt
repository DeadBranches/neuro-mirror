package com.omnivoiceai.neuromirror.ui.screens.questions

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.omnivoiceai.neuromirror.data.database.question.MultipleChoiceQuestion
import com.omnivoiceai.neuromirror.data.database.question.OneShotQuestion
import com.omnivoiceai.neuromirror.data.database.question.Question
import com.omnivoiceai.neuromirror.data.database.question.QuestionType
import com.omnivoiceai.neuromirror.data.repositories.QuestionRepository
import kotlinx.coroutines.launch

@Composable
fun OneShotQuestionComponent(
    question: Question,
    oneShotQuestion: OneShotQuestion,
    questionRepository: QuestionRepository,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    
    // Load existing answer when component is first composed
    LaunchedEffect(question.id) {
        val existingAnswer = questionRepository.getLatestAnswerForQuestion(question.id)
        selectedOption = existingAnswer?.selectedOptionText
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Question title
        question.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        // Options
        oneShotQuestion.answers.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = { 
                            selectedOption = option
                            // Save the answer
                            coroutineScope.launch {
                                questionRepository.saveAnswer(
                                    questionId = question.id,
                                    selectedOptionText = option
                                )
                            }
                        },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { 
                        selectedOption = option
                        // Save the answer
                        coroutineScope.launch {
                            questionRepository.saveAnswer(
                                questionId = question.id,
                                selectedOptionText = option
                            )
                        }
                    }
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun MultipleChoiceQuestionComponent(
    question: Question,
    multipleChoiceQuestion: MultipleChoiceQuestion,
    questionRepository: QuestionRepository,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope()
    
    // Load existing answer when component is first composed
    LaunchedEffect(question.id) {
        val existingAnswer = questionRepository.getLatestAnswerForQuestion(question.id)
        selectedOption = existingAnswer?.selectedOptionIndex
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Question title
        question.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        // Options
        multipleChoiceQuestion.options.forEachIndexed { index, option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (index == selectedOption),
                        onClick = { 
                            selectedOption = index
                            // Save the answer
                            coroutineScope.launch {
                                questionRepository.saveAnswer(
                                    questionId = question.id,
                                    selectedOptionIndex = index,
                                    selectedOptionText = option
                                )
                            }
                        },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (index == selectedOption),
                    onClick = { 
                        selectedOption = index
                        // Save the answer
                        coroutineScope.launch {
                            questionRepository.saveAnswer(
                                questionId = question.id,
                                selectedOptionIndex = index,
                                selectedOptionText = option
                            )
                        }
                    }
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        
        // Show correct answer if one is selected
        if (selectedOption != null) {
            Spacer(modifier = Modifier.height(16.dp))
            val isCorrect = selectedOption == multipleChoiceQuestion.correctIndex
            Text(
                text = if (isCorrect) "Correct!" else "The correct answer is: ${multipleChoiceQuestion.options[multipleChoiceQuestion.correctIndex]}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun ShortTextQuestionComponent(
    question: Question,
    questionRepository: QuestionRepository,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    
    // Load existing answer when component is first composed
    LaunchedEffect(question.id) {
        val existingAnswer = questionRepository.getLatestAnswerForQuestion(question.id)
        text = existingAnswer?.answerText ?: ""
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Question title
        question.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        // Text field for short answer
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                // Save the answer with a slight delay to avoid too many database writes
                coroutineScope.launch {
                    questionRepository.saveAnswer(
                        questionId = question.id,
                        answerText = newText
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            placeholder = { Text("Your answer") },
            singleLine = true
        )
    }
}

@Composable
fun LongTextQuestionComponent(
    question: Question,
    questionRepository: QuestionRepository,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    
    // Load existing answer when component is first composed
    LaunchedEffect(question.id) {
        val existingAnswer = questionRepository.getLatestAnswerForQuestion(question.id)
        text = existingAnswer?.answerText ?: ""
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Question title
        question.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        // Text field for long answer
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                // Save the answer with a slight delay to avoid too many database writes
                coroutineScope.launch {
                    questionRepository.saveAnswer(
                        questionId = question.id,
                        answerText = newText
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(vertical = 8.dp),
            placeholder = { Text("Your answer") },
            maxLines = 5
        )
    }
}

@Composable
fun DefaultQuestionComponent(
    question: Question,
    questionRepository: QuestionRepository? = null
) {
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