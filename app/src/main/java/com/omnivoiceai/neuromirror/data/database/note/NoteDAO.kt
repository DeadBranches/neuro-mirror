package com.omnivoiceai.neuromirror.data.database.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getNoteWithQuestions(noteId: Int): NoteWithQuestions

    @Transaction
    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getNoteWithQuestionsAndAnswer(noteId: Int): NoteWithQuestionsAndAnswers

    @Query("SELECT * FROM note ORDER BY created_at DESC")
    fun getAll(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY created_at DESC")
    fun getAllRaw(): List<Note>


    @Upsert
    suspend fun upsert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM question WHERE note_id = :noteId")
    suspend fun deleteQuestionsByNoteId(noteId: Int)

    @Query("SELECT COUNT(*) FROM note")
    suspend fun countNotes(): Int

    @Query("SELECT COUNT(*) FROM note WHERE emotion_detected = :emotion")
    suspend fun countByEmotion(emotion: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<Note>)

}
