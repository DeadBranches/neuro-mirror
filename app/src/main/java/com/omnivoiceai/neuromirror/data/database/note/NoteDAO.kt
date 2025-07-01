package com.omnivoiceai.neuromirror.data.database.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getNoteWithQuestions(noteId: Int): NoteWithQuestions

    @Query("SELECT * FROM note ORDER BY created_at DESC")
    fun getAll(): Flow<List<Note>>


    @Upsert
    suspend fun upsert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM question WHERE note_id = :noteId")
    suspend fun deleteQuestionsByNoteId(noteId: Int)
}
