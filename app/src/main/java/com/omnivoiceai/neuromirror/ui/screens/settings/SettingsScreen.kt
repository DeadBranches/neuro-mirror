package com.omnivoiceai.neuromirror.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeSettings
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel

@Composable
fun SettingsScreen(navController: NavController, themeViewModel: ThemeViewModel){
    val themeState by themeViewModel.state.collectAsStateWithLifecycle()
    Column {
        Text("Theme Settings",
            style = MaterialTheme.typography.titleLarge)
        ThemeSettings(themeState, themeViewModel::changeTheme )
    }
}