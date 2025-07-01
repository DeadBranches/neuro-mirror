package com.omnivoiceai.neuromirror.data.repositories

import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.database.note.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val notesDAO: NoteDao) {
    val notes: Flow<List<Note>> = notesDAO.getAll()

    suspend fun upsert(todo: Note) = notesDAO.upsert(todo)
    suspend fun delete(todo: Note) = notesDAO.delete(todo)
}