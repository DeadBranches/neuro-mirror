package com.omnivoiceai.neuromirror.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeSettings
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel

@Composable
fun SettingsScreen(navController: NavController, themeViewModel: ThemeViewModel){
    val themeState by themeViewModel.state.collectAsStateWithLifecycle()
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, 8.dp)
    ){
        Text(
            stringResource(
                R.string.settings_page_theme_title),
            style = MaterialTheme.typography.titleLarge)
        ThemeSettings(themeState, themeViewModel::changeTheme )
    }
}