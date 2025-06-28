package com.omnivoiceai.neuromirror.data.database.question

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface QuestionDao {

    @Transaction
    @Query("SELECT * FROM Question")
    suspend fun getAll(): List<QuestionWithDetails>

    @Insert
    suspend fun insertQuestion(question: Question)

    @Insert
    suspend fun insertOneShot(oneShot: OneShotQuestion)

    @Insert
    suspend fun insertMultipleChoice(multi: MultipleChoiceQuestion)
}
