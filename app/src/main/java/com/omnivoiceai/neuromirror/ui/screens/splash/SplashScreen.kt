package com.omnivoiceai.neuromirror.ui.screens.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.ui.screens.splash.components.GoToHomeButton


@Composable
fun SplashScreen(navController: NavHostController, modifier: Modifier = Modifier ){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent,
    ) {
        GoToHomeButton(navController = navController)
    }
}
