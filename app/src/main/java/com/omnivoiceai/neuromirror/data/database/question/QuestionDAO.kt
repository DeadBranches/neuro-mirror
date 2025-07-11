package com.omnivoiceai.neuromirror.data.database.question

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface QuestionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneShotQuestion(oneShotQuestion: OneShotQuestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionAnswer(answer: QuestionAnswer): Long

    @Update
    suspend fun updateQuestionAnswer(answer: QuestionAnswer)

    @Transaction
    @Query("SELECT * FROM Question")
    suspend fun getAll(): List<Question>

    @Transaction
    @Query("SELECT COUNT(DISTINCT note_id) FROM Question")
    suspend fun getAllGroupedByNoteId(): Int

    @Transaction
    @Query("SELECT * FROM OneShotQuestion")
    suspend fun getAllOneShots(): List<OneShotQuestion>

    @Transaction
    @Query("SELECT * FROM MultipleChoiceQuestion")
    suspend fun getAllMultipleChoice(): List<MultipleChoiceQuestion>

    @Transaction
    @Query("SELECT * FROM Question")
    suspend fun getQuestionsWithAnswers(): List<QuestionWithAnswer>

    @Query("SELECT * FROM Question WHERE note_id = :noteId")
    suspend fun getQuestionsByNoteId(noteId: Int): List<Question>

    @Transaction
    @Query("SELECT * FROM Question WHERE note_id = :noteId")
    suspend fun getQuestionsWithDetailsByNoteId(noteId: Int): List<QuestionWithDetails>

    @Transaction
    @Query("SELECT * FROM Question WHERE note_id = :noteId")
    suspend fun getQuestionsWithAnswersByNoteId(noteId: Int): List<QuestionWithAnswer>

    @Query("SELECT * FROM question_answers WHERE question_id = :questionId ORDER BY created_at DESC LIMIT 1")
    suspend fun getLatestAnswerForQuestion(questionId: Int): QuestionAnswer?

    @Query("DELETE FROM question_answers WHERE question_id = :questionId")
    suspend fun deleteAnswersForQuestion(questionId: Int)

    @Query("DELETE FROM Question WHERE note_id = :noteId")
    suspend fun deleteQuestionsByNoteId(noteId: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuestions(list: List<Question>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllOneShots(list: List<OneShotQuestion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMultipleChoices(list: List<MultipleChoiceQuestion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAnswers(list: List<QuestionAnswer>)

}
