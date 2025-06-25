package com.omnivoiceai.neuromirror.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.omnivoiceai.neuromirror.ui.screens.home.HomeScreen
import com.omnivoiceai.neuromirror.ui.screens.splash.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.SplashScreen,
        modifier = modifier
    ) {
        composable<NavigationRoute.SplashScreen> {
            SplashScreen(navController)
        }
        composable<NavigationRoute.HomeScreen> {
            HomeScreen(navController)
        }
    }
}