package com.omnivoiceai.neuromirror.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.omnivoiceai.neuromirror.ui.screens.home.HomeScreen
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesViewModel
import com.omnivoiceai.neuromirror.ui.screens.settings.SettingsScreen
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel
import com.omnivoiceai.neuromirror.ui.screens.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    theme: ThemeViewModel,
    modifier: Modifier = Modifier
) {
    val notesViewModel = koinViewModel<NotesViewModel>()
    val notesState by notesViewModel.state.collectAsStateWithLifecycle()
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.SplashScreen,
        modifier = modifier
    ) {
        composable<NavigationRoute.SplashScreen> {
            SplashScreen(navController)
        }
        composable<NavigationRoute.HomeScreen> {
            HomeScreen(notesState=notesState, notesViewModel=notesViewModel, navController=navController)
        }
        composable<NavigationRoute.SettingsScreen> {
            SettingsScreen(navController, theme)
        }
    }
}