package com.omnivoiceai.neuromirror.data.repositories

import com.omnivoiceai.neuromirror.data.database.note.EmotionDetected
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.database.note.NoteDao
import com.omnivoiceai.neuromirror.data.database.note.NoteWithQuestions
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val notesDAO: NoteDao) {
    val notes: Flow<List<Note>> = notesDAO.getAll()

    suspend fun upsert(note: Note) = notesDAO.upsert(note)
    suspend fun delete(note: Note) = notesDAO.delete(note)
    suspend fun getNoteWithQuestions(noteId: Int): NoteWithQuestions = notesDAO.getNoteWithQuestions(noteId)
    suspend fun countNotes(): Int = notesDAO.countNotes()
    suspend fun countNotesByEmotion(emotion: EmotionDetected): Int = notesDAO.countByEmotion(emotion.name)

    suspend fun getEmotionCounts(): List<Pair<EmotionDetected, Int>> {
        return EmotionDetected.entries.map { emotion ->
            emotion to notesDAO.countByEmotion(emotion.name)
        }
    }
}