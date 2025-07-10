package com.omnivoiceai.neuromirror.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.omnivoiceai.neuromirror.domain.model.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LanguageRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _languageFlow = MutableStateFlow(loadSavedLanguage())
    val language: StateFlow<Language> = _languageFlow

    fun getCurrentLanguage(): Language = _languageFlow.value

    private fun loadSavedLanguage(): Language {
        val code = prefs.getString("language", "en") ?: "en"
        return Language.entries.find { it.code == code } ?: Language.English
    }

    suspend fun setLanguage(language: Language) {
        prefs.edit().putString("language", language.code).apply()
        _languageFlow.emit(language)
    }
}
