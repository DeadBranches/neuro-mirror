package com.omnivoiceai.neuromirror.ui.screens.settings.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omnivoiceai.neuromirror.domain.model.Theme
import com.omnivoiceai.neuromirror.data.repositories.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ThemeState(val theme: Theme) {
    val isDarkTheme: Boolean
        @Composable get() = when (theme) {
            Theme.Light -> false
            Theme.Dark -> true
            Theme.System -> isSystemInDarkTheme()
        }
}

class ThemeViewModel(
    private val repository: ThemeRepository
) : ViewModel() {
    val state = repository.theme.map { ThemeState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ThemeState(Theme.System)
    )

    fun changeTheme(theme: Theme) = viewModelScope.launch {
            repository.setTheme(theme)
    }
}