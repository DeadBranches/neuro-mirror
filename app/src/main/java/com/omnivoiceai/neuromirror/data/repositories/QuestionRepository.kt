package com.omnivoiceai.neuromirror.data.repositories

import com.omnivoiceai.neuromirror.data.database.note.NoteDao
import com.omnivoiceai.neuromirror.data.database.question.MultipleChoiceQuestion
import com.omnivoiceai.neuromirror.data.database.question.OneShotQuestion
import com.omnivoiceai.neuromirror.data.database.question.Question
import com.omnivoiceai.neuromirror.data.database.question.QuestionAnswer
import com.omnivoiceai.neuromirror.data.database.question.QuestionDAO
import com.omnivoiceai.neuromirror.data.database.question.QuestionType
import com.omnivoiceai.neuromirror.data.database.question.QuestionWithAnswer
import com.omnivoiceai.neuromirror.data.database.question.QuestionWithDetails
import com.omnivoiceai.neuromirror.data.remote.QuestionData
import com.omnivoiceai.neuromirror.utils.Logger

class QuestionRepository(
    private val questionDao: QuestionDAO,
    private val noteDao: NoteDao
) {
    
    suspend fun saveQuestions(questions: List<QuestionData>, noteId: Int) {
        try {
            for (questionData in questions) {
                val type = when (questionData.type.lowercase()) {
                    "oneshot" -> QuestionType.Oneshot
                    "multiple" -> QuestionType.Multiple
                    "shorttext" -> QuestionType.ShortText
                    "longtext" -> QuestionType.LongText
                    else -> QuestionType.ShortText
                }
                
                val question = Question(
                    noteId = noteId,
                    title = questionData.title,
                    type = type
                )
                
                val questionId = questionDao.insertQuestion(question).toInt()
                
                when (type) {
                    QuestionType.Oneshot -> {
                        val oneShotQuestion = OneShotQuestion(
                            questionId = questionId,
                            answers = questionData.options
                        )
                        questionDao.insertOneShotQuestion(oneShotQuestion)
                    }
                    
                    QuestionType.Multiple -> {
                        val multipleChoiceQuestion = MultipleChoiceQuestion(
                            questionId = questionId,
                            options = questionData.options,
                            correctIndex = questionData.correctIndex ?: 0
                        )
                        questionDao.insertMultipleChoiceQuestion(multipleChoiceQuestion)
                    }
                    
                    QuestionType.ShortText, QuestionType.LongText -> {
                        // These types don't need additional data
                    }
                }
            }
        } catch (e: Exception) {
            Logger.error("Failed to save questions: ${e.message}", e)
            throw Exception("Failed to save questions: ${e.message}")
        }
    }
    
    suspend fun getQuestionsWithDetailsByNoteId(noteId: Int): List<QuestionWithDetails> {
        return questionDao.getQuestionsWithDetailsByNoteId(noteId)
    }

    suspend fun getQuestionsWithAnswersByNoteId(noteId: Int): List<QuestionWithAnswer> {
        return questionDao.getQuestionsWithAnswersByNoteId(noteId)
    }

    suspend fun saveAnswer(questionId: Int, answerText: String? = null, selectedOptionIndex: Int? = null, selectedOptionText: String? = null) {
        val answer = QuestionAnswer(
            questionId = questionId,
            answerText = answerText,
            selectedOptionIndex = selectedOptionIndex,
            selectedOptionText = selectedOptionText
        )
        
        // Delete existing answers for this question first
        questionDao.deleteAnswersForQuestion(questionId)
        
        // Insert the new answer
        questionDao.insertQuestionAnswer(answer)
    }

    suspend fun getLatestAnswerForQuestion(questionId: Int): QuestionAnswer? {
        return questionDao.getLatestAnswerForQuestion(questionId)
    }
} 