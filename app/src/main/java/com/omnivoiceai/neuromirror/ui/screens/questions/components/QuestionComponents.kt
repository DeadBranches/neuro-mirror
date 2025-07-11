package com.omnivoiceai.neuromirror.ui.screens.questions.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.question.MultipleChoiceQuestion
import com.omnivoiceai.neuromirror.data.database.question.OneShotQuestion
import com.omnivoiceai.neuromirror.data.database.question.Question
import kotlinx.coroutines.launch

@Composable
fun OneShotQuestionComponent(
    question: Question,
    oneShotQuestion: OneShotQuestion,
    onLoadAnswer: suspend (Int) -> String?,
    onSaveAnswer: suspend (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Load existing answer
    LaunchedEffect(question.id) {
        selectedOption = onLoadAnswer(question.id)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        question.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        oneShotQuestion.answers.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = {
                            selectedOption = option
                            coroutineScope.launch {
                                onSaveAnswer(question.id, option)
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
                        coroutineScope.launch {
                            onSaveAnswer(question.id, option)
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
    onLoadAnswer: suspend (Int) -> Int?, // questionId → selectedIndex
    onSaveAnswer: suspend (questionId: Int, selectedIndex: Int, selectedText: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(question.id) {
        selectedOption = onLoadAnswer(question.id)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        question.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        multipleChoiceQuestion.options.forEachIndexed { index, option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (index == selectedOption),
                        onClick = {
                            selectedOption = index
                            coroutineScope.launch {
                                onSaveAnswer(question.id, index, option)
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
                        coroutineScope.launch {
                            onSaveAnswer(question.id, index, option)
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

        selectedOption?.let {
            Spacer(modifier = Modifier.height(16.dp))
            val isCorrect = it == multipleChoiceQuestion.correctIndex
            Text(
                text = if (isCorrect) stringResource(R.string.correct) else "${stringResource(R.string.wrong_answer)} ${multipleChoiceQuestion.options[multipleChoiceQuestion.correctIndex]}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}


@Composable
fun ShortTextQuestionComponent(
    question: Question,
    onLoadAnswer: suspend (Int) -> String?,
    onSaveAnswer: suspend (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(question.id) {
        text = onLoadAnswer(question.id) ?: ""
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        question.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                coroutineScope.launch {
                    onSaveAnswer(question.id, newText)
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
    onLoadAnswer: suspend (Int) -> String?,
    onSaveAnswer: suspend (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(question.id) {
        text = onLoadAnswer(question.id) ?: ""
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        question.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                coroutineScope.launch {
                    onSaveAnswer(question.id, newText)
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