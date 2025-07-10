package com.omnivoiceai.neuromirror.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.screens.auth.login.LoginViewModel
import com.omnivoiceai.neuromirror.ui.screens.settings.components.SettingLineItem
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel

@Composable
fun SettingsScreen(loginViewModel: LoginViewModel, navController: NavHostController, themeViewModel: ThemeViewModel){
    val themeState by themeViewModel.state.collectAsStateWithLifecycle()
    val currentUser by loginViewModel.currentUser.collectAsStateWithLifecycle()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, 8.dp)
    ){
        val themeTitle = stringResource(R.string.settings_page_theme_title)
        SettingLineItem(title = themeTitle, navController = {
            navController.navigate(NavigationRoute.SettingsSubScreen(themeTitle))
        })
        val notificationTitle = stringResource(R.string.settings_notifications)
        SettingLineItem(title = notificationTitle, navController = {
            navController.navigate(NavigationRoute.SettingsSubScreen(notificationTitle))
        })
        val languageTitle = stringResource(R.string.language)
        SettingLineItem(title = languageTitle, navController = {
            navController.navigate(NavigationRoute.SettingsSubScreen("Language"))
        })

        Spacer(modifier = Modifier.height(24.dp))
        
        if(currentUser != null) {
            Button(onClick = { 
                loginViewModel.signOut()
            }) {
                Text("Logout")
            }
        } else {
            Button(onClick = { 
                navController.navigate(NavigationRoute.SplashScreen) {
                    popUpTo(0) { inclusive = true }
                }
            }) {
                Text("Login")
            }
        }
    }
}