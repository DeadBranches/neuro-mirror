package com.omnivoiceai.neuromirror.ui.screens.settings.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.omnivoiceai.neuromirror.domain.model.Theme
import com.omnivoiceai.neuromirror.ui.components.forms.RadioSelector

@Composable
fun ThemeSettings(state: ThemeState, onThemeSelected: (Theme) -> Unit) {
    RadioSelector(
        items = Theme.entries,
        selectedItem = state.theme,
        onItemSelected = onThemeSelected,
        labelProvider = { stringResource(it.labelResId) }
    )
}
