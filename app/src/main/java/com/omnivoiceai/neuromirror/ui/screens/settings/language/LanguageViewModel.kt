package com.omnivoiceai.neuromirror.ui.screens.settings.language

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omnivoiceai.neuromirror.data.repositories.LanguageRepository
import com.omnivoiceai.neuromirror.domain.model.Language
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LanguageState(val language: Language)

class LanguageViewModel(
    private val repository: LanguageRepository
) : ViewModel() {

    val state = repository.language
        .map { LanguageState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LanguageState(repository.getCurrentLanguage())
        )

    fun changeLanguage(language: Language, context: Context) = viewModelScope.launch {
        if(language == state.value.language){
            return@launch
        }
        repository.setLanguage(language)
        (context as? Activity)?.recreate()
    }
}
