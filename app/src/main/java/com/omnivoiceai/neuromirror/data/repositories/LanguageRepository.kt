package com.omnivoiceai.neuromirror.data.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.omnivoiceai.neuromirror.domain.model.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LanguageRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _languageFlow = MutableStateFlow(loadSavedLanguage())
    val language: StateFlow<Language> = _languageFlow

    fun getCurrentLanguage(): Language = _languageFlow.value

    private fun languageToCheck(code: String): Language{
        return Language.entries.find { it.code == code } ?: Language.English
    }

    private fun loadSavedLanguage(): Language {
        val savedCode = prefs.getString("language", null)

        if (savedCode != null) {
            return languageToCheck(savedCode)
        }

        val systemLocale = java.util.Locale.getDefault().language
        return languageToCheck(systemLocale)
    }

    suspend fun setLanguage(language: Language) {
        prefs.edit { putString("language", language.code) }
        _languageFlow.emit(language)
    }
}
