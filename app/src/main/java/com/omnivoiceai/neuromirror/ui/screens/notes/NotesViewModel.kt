package com.omnivoiceai.neuromirror.ui.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.repositories.NoteRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NoteRepository): ViewModel() {
    val state = repository.notes.map { NotesState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = NotesState(emptyList())
    )

    val actions = object : NotesActions {

        override fun addNote(note: Note): Job = viewModelScope.launch {
            repository.upsert(note)
        }

        override fun removeNote(note: Note): Job = viewModelScope.launch {
            repository.delete(note)
        }

        override fun generateQuestions(note: Note): Job = viewModelScope.launch {
            throw Error("not implemented")
        }

    }
}