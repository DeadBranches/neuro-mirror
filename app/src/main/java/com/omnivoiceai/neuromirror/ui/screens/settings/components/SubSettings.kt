package com.omnivoiceai.neuromirror.ui.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeSettings
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

enum class SubSettings {
    Theme
}
@Composable
fun SettingsSubScreen(
    title: String,
    modifier: Modifier = Modifier
) {
    val themeViewModel = koinViewModel<ThemeViewModel>()
    val themeState = themeViewModel.state.collectAsStateWithLifecycle().value

    val content: @Composable ColumnScope.() -> Unit = when (title) {
        stringResource(R.string.settings_page_theme_title) -> { { ThemeSettings(themeState, themeViewModel::changeTheme) } }
//        "Notifiche" -> { { NotificationSettingsContent() } }
//        "Privacy" -> { { PrivacySettingsContent() } }
        else -> { { Text("Sezione non trovata.") } }
    }

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp), content = content)
}
