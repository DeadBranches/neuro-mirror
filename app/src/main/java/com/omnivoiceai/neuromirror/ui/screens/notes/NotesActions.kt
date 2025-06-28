package com.omnivoiceai.neuromirror.ui.screens.notes

import com.omnivoiceai.neuromirror.data.database.note.Note
import kotlinx.coroutines.Job

interface NotesActions {
    fun addNote(note: Note): Job
    fun removeNote(note: Note): Job

    fun generateQuestions(note:Note): Job
}