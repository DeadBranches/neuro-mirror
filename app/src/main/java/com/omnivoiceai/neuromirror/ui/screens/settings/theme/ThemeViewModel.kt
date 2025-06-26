package com.omnivoiceai.neuromirror.ui.screens.settings.theme

import androidx.lifecycle.ViewModel
import com.omnivoiceai.neuromirror.data.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ThemeState(val theme: Theme)
class ThemeViewModel : ViewModel() {
    private val _state = MutableStateFlow(ThemeState(Theme.System))
    val state = _state.asStateFlow()

    fun changeTheme(theme: Theme) {
        _state.value = ThemeState(theme)
    }
}