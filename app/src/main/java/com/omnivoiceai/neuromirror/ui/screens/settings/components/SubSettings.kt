package com.omnivoiceai.neuromirror.ui.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.screens.settings.language.LanguageSettings
import com.omnivoiceai.neuromirror.ui.screens.settings.language.LanguageViewModel
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeSettings
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun SettingsSubScreen(
    title: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val themeViewModel = koinViewModel<ThemeViewModel>()
    val themeState = themeViewModel.state.collectAsStateWithLifecycle().value
    val languageViewModel = koinInject<LanguageViewModel>()
    val languageState = languageViewModel.state.collectAsStateWithLifecycle().value


    val content: @Composable ColumnScope.() -> Unit = when (title) {
        stringResource(R.string.settings_page_theme_title) -> { { ThemeSettings(themeState, themeViewModel::changeTheme) } }
        stringResource(R.string.settings_notifications) -> { { NotificationScreen() } }
        "Language" -> {
            {
                LanguageSettings(
                    state = languageState,
                    onLanguageSelect = { language ->
                        languageViewModel.changeLanguage(language, context)
                    }
                )
            }
        }
        else -> { { Text(stringResource(R.string.not_found)) } }
    }

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp), content = content)
}
