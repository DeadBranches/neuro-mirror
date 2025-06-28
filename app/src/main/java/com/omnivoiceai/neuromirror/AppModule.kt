package com.omnivoiceai.neuromirror

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.omnivoiceai.neuromirror.data.database.AppDatabase
import com.omnivoiceai.neuromirror.data.repositories.NoteRepository
import com.omnivoiceai.neuromirror.data.repositories.ThemeRepository
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesViewModel
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("theme")

val appModule = module {
    single { get<Context>().dataStore }
    single { ThemeRepository(get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { NotesViewModel(get()) }
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "note-list"
        ).build()
    }
    single { NoteRepository(get<AppDatabase>().noteDao()) }

}