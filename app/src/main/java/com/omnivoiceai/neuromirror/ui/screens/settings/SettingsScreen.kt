package com.omnivoiceai.neuromirror.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.BuildConfig
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.screens.auth.login.LoginViewModel
import com.omnivoiceai.neuromirror.ui.screens.settings.components.BackupButtons
import com.omnivoiceai.neuromirror.ui.screens.settings.components.GitHubLink
import com.omnivoiceai.neuromirror.ui.screens.settings.components.SettingLineItem
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel

@Composable
fun SettingsScreen(loginViewModel: LoginViewModel, navController: NavHostController, themeViewModel: ThemeViewModel){
    val themeState by themeViewModel.state.collectAsStateWithLifecycle()
    val currentUser by loginViewModel.currentUser.collectAsStateWithLifecycle()

    val versionCode = BuildConfig.VERSION_CODE
    val versionName = BuildConfig.VERSION_NAME
    val applicationId = BuildConfig.APPLICATION_ID

    val style = MaterialTheme.typography.headlineSmall


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, 8.dp)
    ){
        Text(stringResource(R.string.general),
            modifier = Modifier.padding(top = 16.dp ),
            style = MaterialTheme.typography.headlineSmall,
        )

        val themeTitle = stringResource(R.string.settings_page_theme_title)
        SettingLineItem(title = themeTitle, onClick = {
            navController.navigate(NavigationRoute.SettingsSubScreen(themeTitle))
        })
        val notificationTitle = stringResource(R.string.settings_notifications)
        SettingLineItem(title = notificationTitle, onClick = {
            navController.navigate(NavigationRoute.SettingsSubScreen(notificationTitle))
        })
        val languageTitle = stringResource(R.string.language)
        SettingLineItem(title = languageTitle, onClick = {
            navController.navigate(NavigationRoute.SettingsSubScreen("Language"))
        })

        HorizontalDivider()

//        Spacer(modifier = Modifier.height(24.dp))

        BackupButtons()

        HorizontalDivider()

        Text(stringResource(R.string.access),
            modifier = Modifier.padding(top = 16.dp ),
            style = MaterialTheme.typography.headlineSmall,
        )

        if(currentUser != null) {
            SettingLineItem(title = stringResource(R.string.logout), onClick = {
                loginViewModel.signOut()
            })
        } else {
            SettingLineItem(title = stringResource(R.string.login), onClick = {
                navController.navigate(NavigationRoute.SplashScreen) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
        HorizontalDivider()

        Text(stringResource(R.string.version),
            modifier = Modifier.padding(top = 16.dp ),
            style = MaterialTheme.typography.headlineSmall,
        )
        EmptySpacer()
        Text(stringResource(R.string.app_name) + " " + versionName,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall
        )
        Text(applicationId,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall
        )
        GitHubLink()
    }
}