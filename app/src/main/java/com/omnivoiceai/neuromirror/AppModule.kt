package com.omnivoiceai.neuromirror

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.omnivoiceai.neuromirror.data.repositories.ThemeRepository
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("theme")

val appModule = module {
    single { get<Context>().dataStore }
    single { ThemeRepository(get()) }
    viewModel { ThemeViewModel(get()) }
}