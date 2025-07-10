package com.omnivoiceai.neuromirror.ui.screens.settings.language

import androidx.compose.runtime.Composable
import com.omnivoiceai.neuromirror.domain.model.Language
import com.omnivoiceai.neuromirror.ui.components.forms.RadioSelector

@Composable
fun LanguageSettings(state: LanguageState, onLanguageSelect: (Language) -> Unit) {
    RadioSelector(
        items = Language.entries,
        selectedItem = state.language,
        onItemSelected = onLanguageSelect,
        labelProvider = { it.toString() }
    )
}
